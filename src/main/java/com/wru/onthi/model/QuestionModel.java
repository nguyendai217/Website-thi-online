package com.wru.onthi.model;

import java.util.List;

public class QuestionModel {
    private String content;
    private List<String> listAns;
    private String ansCorrect;
    private String yourAns;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getListAns() {
        return listAns;
    }

    public void setListAns(List<String> listAns) {
        this.listAns = listAns;
    }

    public String getAnsCorrect() {
        return ansCorrect;
    }

    public void setAnsCorrect(String ansCorrect) {
        this.ansCorrect = ansCorrect;
    }

    public String getYourAns() {
        return yourAns;
    }

    public void setYourAns(String yourAns) {
        this.yourAns = yourAns;
    }
}
