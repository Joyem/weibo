package com.tyella.weibo.service;

import com.tyella.weibo.dao.LoginTicketDao;
import com.tyella.weibo.dao.UserDao;
import com.tyella.weibo.model.LoginTicket;
import com.tyella.weibo.model.User;
import com.tyella.weibo.util.WeiboUtil;
import com.tyella.weibo.util.IllegalStrFilterUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by tyella on 2019/2/12.
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTicketDao loginTicketDao;

    public Map<String, Object> register(String userName, String password) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(userName)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        //防止sql注入攻击
        if(!IllegalStrFilterUtil.sqlStrFilter(userName) || !IllegalStrFilterUtil.sqlStrFilter(password)){
            map.put("msg","不安全的输入，请重新输入");
            return map;
        }

        //用户名校验
        User user = userDao.selectByName(userName);
        if (user != null) {
            map.put("msg", "该用户名已被注册");
            return map;
        }

        user = new User();
        user.setName(userName);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.tyella.com/head/%dt.png", 1));
        user.setPassword(password + WeiboUtil.MD5(user.getSalt()));
        userDao.addUser(user);

        //传入ticket,也就是登陆成功
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public Map<String, Object> login(String userName, String password) {
        Map<String, Object> map = new HashMap<>();
        //用户名为空
        if (StringUtils.isBlank(userName)) {
            map.put("msg", "用户名不能为空！");
            return map;
        }
        //密码为空
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空!");
            return map;
        }
        User user = userDao.selectByName(userName);
        //用户名不存在
        if (user == null) {
            map.put("msg", "用户名不存在!");
            return map;
        }
        //密码错误
        if (!user.getPassword().equals(WeiboUtil.MD5(password + user.getSalt()))) {
            map.put("msg", "密码错误!");
            return map;
        }

        //防止sql注入攻击
        if(!IllegalStrFilterUtil.sqlStrFilter(userName) || !IllegalStrFilterUtil.sqlStrFilter(password)){
            map.put("msg","不安全的输入，请重新输入");
            return map;
        }

        //传入ticket,也就是登陆成功
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 24);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDao.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public void addUser(User user) {
        userDao.addUser(user);
    }

    public void deleteUser(int userId) {
        userDao.deleteById(userId);
    }

    public User getUser(int id) {
        return userDao.selectById(id);
    }

    public void logout(String ticket) {
        loginTicketDao.updateStatus(ticket, 1);
    }

}
