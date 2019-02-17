package com.tyella.weibo.controller;

import com.tyella.weibo.model.*;
import com.tyella.weibo.service.*;
import com.tyella.weibo.util.WeiboUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;

    @Autowired
    LikeService likeService;

    @Autowired
    UserService userService;

    @Autowired
    UserHolder userHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    AliService aliService;

    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model) {
        News news = newsService.getNews(newsId);
        if (news != null) {
            int localUserId = userHolder.getUser() != null ? userHolder.getUser().getId() : 0;
            if (localUserId != 0) {
                //用户已登陆
                model.addAttribute("like", likeService.getLikeStatus(localUserId, news.getId(), EntityType.ENTITY_NEWS));
            } else {
                //用户未登陆
                model.addAttribute("like", 0);
            }
            //评论
            List<Comment> comments = commentService.getCommentByEntity(news.getId(), EntityType.ENTITY_NEWS);
            List<ViewObject> commentVOs = new ArrayList<>();
            for (Comment comment : comments) {
                ViewObject vo = new ViewObject();
                vo.set("comment", comment);
                vo.set("user", userService.getUser(comment.getUserId()));
                commentVOs.add(vo);
            }
            model.addAttribute("comments", commentVOs);
        }
        model.addAttribute("news", news);
        model.addAttribute("owner", userService.getUser(news.getUserId()));
        return "detail";
    }

    //todo
    //put post 区别
    @RequestMapping(path = {"/user/addNews/"},method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try{
            News news= new News();
            news.setImage(image);
            news.setTitle(title);
            news.setLink(link);
            news.setCreatedDate(new Date());
            if(userHolder.getUser()!=null){
                news.setUserId(userHolder.getUser().getId());
            }else{
                //设置一个匿名用户
                news.setUserId(3);
            }
            newsService.addNews(news);
            return WeiboUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("添加资讯失败"+e.getMessage());
            return WeiboUtil.getJSONString(1,"发布失败");
        }
    }

    @RequestMapping(path = {"/user/addComment/"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("newId") int newsId,@RequestParam("content") String content){
        try{
            Comment comment=new Comment();
            comment.setUserId(userHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_COMMENT);
            comment.setCreateTime(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);

            //todo
            //更新评论数量，以后用异步实现
            int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(),count);
        }catch(Exception e){
            logger.error("提交评论错误",e.getMessage());
        }
        return "redirect:/news/"+String.valueOf(newsId);
    }

    @RequestMapping("/uploadImage/")
    @ResponseBody
    public String upLoadOSS(MultipartFile file){
        try{
            String fileUrl=aliService.saveImage(file);
            if(fileUrl==null){
                return WeiboUtil.getJSONString(1,"上传图片失败");
            }
            return WeiboUtil.getJSONString(0,fileUrl);
        }catch(IOException e){
            logger.error("上传失败"+e.getMessage());
            return WeiboUtil.getJSONString(1,"上传失败");
        }
    }


    //todo
    //HttpServletResponse   StreamUtils.copy
    @RequestMapping(path = {"/image/"},method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName, HttpServletResponse response) {
        try {
            response.setContentType("image/jpg");
            StreamUtils.copy(new FileInputStream(new File(WeiboUtil.IMAGE_DIR + imageName)), response.getOutputStream());
        } catch (Exception e) {
            logger.error("读取图片错误" + e.getMessage());
        }
    }

}
