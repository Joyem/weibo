package com.tyella.weibo.model;

import java.util.Date;

/**
 * Created by tyella on 2019/2/11.
 */
public class News {

    private int id;

    private String title;

    private String link;

    private String image;

    private Date createdDate;

    private int userId;

    private int likeCount;

    private int commentCount;

    public News(){

    }

    public News(int id, String title, String link, String image, Date createdDate, int userId, int likeCount, int commentCount) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.image = image;
        this.createdDate = createdDate;
        this.userId = userId;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
