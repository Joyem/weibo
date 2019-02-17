package com.tyella.weibo.interceptor;

import com.tyella.weibo.dao.LoginTicketDao;
import com.tyella.weibo.dao.UserDao;
import com.tyella.weibo.model.LoginTicket;
import com.tyella.weibo.model.User;
import com.tyella.weibo.model.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

//ture继续请求  flase拒绝请求
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTicketDao loginTicketDao;

    @Autowired
    private UserHolder userHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,Object o) throws Exception{
        //处理用户信息，判断用户是否有ticket，一个用户一个ticket，但是有时限
        String ticket=null;
        if(httpServletRequest.getCookies()!=null){
            for(Cookie cookie:httpServletRequest.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket=cookie.getValue();
                    break;
                }
            }
            //判断ticket是否有效
            if(ticket!=null){
                LoginTicket loginTicket=loginTicketDao.selectByTicket(ticket);
                if(loginTicket==null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus()!=0){
                    return false;
                }else{
                    //不能放入request中，因为是一个全局的ticket，其它用户可能不会用到httprequest
                    User user=userDao.selectById(loginTicket.getId());
                    userHolder.setUser(user);
                    return true;
                }
            }
        }
        return true;
    }

    //渲染之前提供的后处理方法，可以添加模型数据，自动传给前端。
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if(modelAndView!=null && userHolder.getUser()!=null){
            modelAndView.addObject(userHolder.getUser());
            userHolder.clear();
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
