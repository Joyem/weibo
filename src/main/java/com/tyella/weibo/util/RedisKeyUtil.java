package com.tyella.weibo.util;

public class RedisKeyUtil {
    private static String SPLIT=":";
    private static String RK_LIKE=" LIKE ";
    private static String RK_DISLIKE=" DISLIKE ";
    private static String RK_EVENT=" EVENT ";

    public static String getLikeKey(int entityId,int entityType){
        return RK_LIKE+SPLIT+String.valueOf(entityId)+SPLIT+String.valueOf(entityType);
    }

    public static String getDislikeKey(int entityId,int entityType){
        return RK_DISLIKE+SPLIT+String.valueOf(entityId)+SPLIT+String.valueOf(entityType);
    }

    public static String getEventQueueKey(){
        return RK_EVENT;
    }
}
