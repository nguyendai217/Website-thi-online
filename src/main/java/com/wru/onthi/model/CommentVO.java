package com.wru.onthi.model;

import com.wru.onthi.entity.Comment;

import java.util.List;

public class CommentVO {
    Integer page;
    Integer perPage;
    Integer totalComent;
    List<CommentInfo> listComment;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public Integer getTotalComent() {
        return totalComent;
    }

    public void setTotalComent(Integer totalComent) {
        this.totalComent = totalComent;
    }

    public List<CommentInfo> getListComment() {
        return listComment;
    }

    public void setListComment(List<CommentInfo> listComment) {
        this.listComment = listComment;
    }
}
