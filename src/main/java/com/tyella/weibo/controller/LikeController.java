package com.tyella.weibo.controller;

import com.tyella.weibo.async.EventModel;
import com.tyella.weibo.async.EventProducer;
import com.tyella.weibo.async.EventType;
import com.tyella.weibo.model.EntityType;
import com.tyella.weibo.model.News;
import com.tyella.weibo.model.UserHolder;
import com.tyella.weibo.service.LikeService;
import com.tyella.weibo.service.NewsService;
import com.tyella.weibo.util.WeiboUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserHolder userHolder;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(int newId) {
        long likeCount = likeService.like(userHolder.getUser().getId(), newId, EntityType.ENTITY_NEWS);
        newsService.updateLikeCount(newId, (int) likeCount);
        News news = newsService.getNews(newId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(userHolder.getUser().getId())
                .setEntityId(newId)
                .setEntityOwnerId(news.getUserId()));
        return WeiboUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String disLike(int newId) {
        long likeCount = likeService.disLike(userHolder.getUser().getId(), newId, EntityType.ENTITY_NEWS);
        newsService.updateLikeCount(newId, (int) likeCount);
        return WeiboUtil.getJSONString(0, String.valueOf(likeCount));
    }

}
