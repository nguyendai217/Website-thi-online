package com.wru.onthi.model;

import com.wru.onthi.entity.Comment;
import com.wru.onthi.entity.Lesson;
import com.wru.onthi.entity.User;

public class CommentInfo {
    private Integer id;
    private String timeComment;
    private String contentComment;
    private String userName;
    private String userImage;

    public CommentInfo() {
    }

    public CommentInfo(Comment comment) {
        this.id = comment.getId();
        this.timeComment = comment.getTimeComment();
        this.contentComment = comment.getContentComment();
        this.userName = comment.getUserComment().getFullname();
        this.userImage = comment.getUserComment().getImage();
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTimeComment() {
        return timeComment;
    }

    public void setTimeComment(String timeComment) {
        this.timeComment = timeComment;
    }

    public String getContentComment() {
        return contentComment;
    }

    public void setContentComment(String contentComment) {
        this.contentComment = contentComment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}