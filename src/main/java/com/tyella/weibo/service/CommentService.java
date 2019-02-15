package com.tyella.weibo.service;

import com.tyella.weibo.dao.CommentDao;
import com.tyella.weibo.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tyella on 2019/2/13.
 */
@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    public int addComment(Comment comment){
        return commentDao.addComment(comment);
    }

    public List<Comment> getCommentByEntity(int entityId,int entityType){
        return commentDao.selectByEntity(entityId,entityType);
    }

    public int getCommentCount(int entityId,int entityType){
        return commentDao.getCommentCount(entityId,entityType);
    }

}
