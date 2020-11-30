package com.wru.onthi.entity;

import javax.persistence.*;

@Entity
@Table(name = "result")
public class Result {

    // 1 user cos nhieu ket qua bai thi

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "score")
    private Double score;

    @Column(name = "detail",columnDefinition="TEXT")
    private String detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userResult;

    @OneToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @Column(name = "satus")
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public User getUserResult() {
        return userResult;
    }

    public void setUserResult(User userResult) {
        this.userResult = userResult;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
