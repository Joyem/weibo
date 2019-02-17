package com.tyella.weibo.controller;

import com.tyella.weibo.model.EntityType;
import com.tyella.weibo.model.News;
import com.tyella.weibo.model.UserHolder;
import com.tyella.weibo.model.ViewObject;
import com.tyella.weibo.service.LikeService;
import com.tyella.weibo.service.NewsService;
import com.tyella.weibo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserHolder userHolder;

    @Autowired
    private LikeService likeService;

    private List<ViewObject> getNews(int userId, int offset, int limit) {
        List<News> newsList = newsService.getLastedNews(userId, offset, limit);
        int localUserId = userHolder.getUser() != null ? userHolder.getUser().getId() : 0;
        List<ViewObject> voList = new ArrayList<>();
        for (News news : newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getUserId()));
            if (localUserId != 0) {
                vo.set("like", likeService.getLikeStatus(localUserId, news.getId(), EntityType.ENTITY_NEWS));
            } else {
                vo.set("like", 0);
            }
            voList.add(vo);
        }
        return voList;
    }


}
