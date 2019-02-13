package com.tyella.weibo.service;

import com.tyella.weibo.util.JedisUtil;
import com.tyella.weibo.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private JedisUtil jedisUtil;

    public long like(int userId, int entityId, int entityType) {
        //在喜欢集合里添加
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisUtil.sadd(likeKey, String.valueOf(userId));
        //从不喜欢集合里删除
        String disLikeKey = RedisKeyUtil.getDislikeKey(entityId, entityType);
        jedisUtil.srem(disLikeKey, String.valueOf(userId));
        return jedisUtil.scard(likeKey);
    }

    public long disLike(int userId, int entityId, int entityType) {
        //往不喜欢集合里添加
        String disLikeKey = RedisKeyUtil.getDislikeKey(entityId, entityType);
        jedisUtil.sadd(disLikeKey, String.valueOf(userId));
        //从喜欢的集合里删除
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisUtil.srem(likeKey, String.valueOf(userId));
        return jedisUtil.scard(disLikeKey);
    }

    /**
     * 1:喜欢 -1：不喜欢 0：未知
     * @param userId
     * @param entityId
     * @param entityType
     * @return
     */
    public int getLikeStatus(int userId, int entityId, int entityType) {
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        if (jedisUtil.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDislikeKey(entityId, entityType);
        return jedisUtil.sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;
    }
}