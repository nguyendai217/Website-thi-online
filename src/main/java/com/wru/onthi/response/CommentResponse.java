package com.wru.onthi.response;


import com.wru.onthi.model.CommentInfo;

public class CommentResponse extends ResponseBuild{
    private CommentInfo commentInfor;

    public CommentResponse(CommentInfo commentInfor) {
        super(200, 1, "Thành công");
        this.commentInfor = commentInfor;
    }

    public CommentResponse(String message, CommentInfo commentInfor) {
        super(200, 1, message);
        this.commentInfor = commentInfor;
    }

    public CommentResponse(Integer code, Integer status, String message, CommentInfo commentInfor) {
        super(code, status, message);
        this.commentInfor = commentInfor;
    }

    public CommentInfo getCommentInfor() {
        return commentInfor;
    }

    public void setCommentInfor(CommentInfo commentInfor) {
        this.commentInfor = commentInfor;
    }
}
