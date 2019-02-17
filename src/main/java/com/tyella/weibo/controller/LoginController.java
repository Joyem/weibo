package com.tyella.weibo.controller;

import com.tyella.weibo.async.EventModel;
import com.tyella.weibo.async.EventProducer;
import com.tyella.weibo.async.EventType;
import com.tyella.weibo.service.UserService;
import com.tyella.weibo.util.WeiboUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = {"/reg"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model,
                      @RequestParam("username") String userName,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember", defaultValue = "0") int rememberme,
                      HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.register(userName, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                return WeiboUtil.getJSONString(0, "注册成功");
            } else {
                return WeiboUtil.getJSONString(1, "注册失败");
            }
        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return WeiboUtil.getJSONString(1, "注册异常");
        }
    }

    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model,
                        @RequestParam("username") String userName,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rember", defaultValue = "0") int remember,
                        HttpServletResponse httpServletResponse) {
        try {
            Map<String, Object> map = userService.login(userName, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (remember > 0) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                httpServletResponse.addCookie(cookie);
                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                        .setExts("username", userName)
                        .setExts("email", "272711917@qq.com"));
                return WeiboUtil.getJSONString(0, "登录成功");
            } else {
                return WeiboUtil.getJSONString(1, "登录异常");
            }
        } catch (Exception e) {
            logger.error("登录异常" + e.getMessage());
            return WeiboUtil.getJSONString(1, "登录异常");
        }
    }

    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
