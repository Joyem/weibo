package com.tyella.weibo.model;

import org.springframework.stereotype.Component;

/**
 * 保存当前访问的用户信息，用拦截器拦截时注入
 */
@Component
public class UserHolder {

    //通过ThreadLocal，使得数据(User)被单个线程独占而在类和方法间共享
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
