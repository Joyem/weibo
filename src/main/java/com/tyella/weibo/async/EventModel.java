package com.tyella.weibo.async;

import java.util.HashMap;
import java.util.Map;

//事件实体
public class EventModel {
    private EventType eventType;
    //触发者
    private int actorId;
    //触发事件
    private int entityId;
    private int entityType;
    //触发事件拥有者，拥有者会收到通知
    private int entityOwnerId;
    //触发现场的信息保存
    private Map<String,String> exts=new HashMap<>();

    public EventModel(EventType eventType){
        this.eventType=eventType;
    }

    //使用return this让代码执行链路化操作 set set set
    //后面的set都使用此种方法
    public EventModel setExts(String key,String value){
        exts.put(key,value);
        return this;
    }

    public String getExts(String key){
        return exts.get(key);
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }
}
