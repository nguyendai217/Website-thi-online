package com.wru.onthi.response;

public class ResponseBuild {
    private Integer code;
    private Integer Status;
    private String Message;

    private static final int CODE_OK = 200;
    private static final int CODE_ERR = 400;
    private static final int CODE_ERR_SERVE = 500;
    private static final int CODE_NOT_FOUND = 404;

    public ResponseBuild() {
    }

    public ResponseBuild(Integer code, Integer status, String message) {
        this.code = code;
        this.Status = status;
        this.Message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
