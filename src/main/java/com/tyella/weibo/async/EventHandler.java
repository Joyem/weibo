package com.tyella.weibo.async;

import java.util.List;

/**
 * 用来处理事件的接口
 */
public interface EventHandler {
    //处理事件，不同实现类处理方式不同
    void doHandle(EventModel eventModel);
    //获取该handler可以处理的所有事件类型
    List<EventType> getSupportedEventTypes();
}
