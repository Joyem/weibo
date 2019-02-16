package com.tyella.weibo.async;

/**
 * 事件类型枚举类
 */
//todo
    //枚举的写法
public enum EventType {
    LIKE(0),           //点赞
    COMMIT(1),         //提交
    LOGIN(2);          //登陆

    private int value;
    EventType(int value){
        this.value=value;
    }
}
