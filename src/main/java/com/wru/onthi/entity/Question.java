package com.wru.onthi.entity;

import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "question")
public class Question {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "content")
    private String content;

    @Column(name = "ans_a")
    private String ansA;

    @Column(name = "ans_b")
    private String ansB;

    @Column(name = "ans_c")
    private String ansC;

    @Column(name = "ans_d")
    private String ansD;

    @Column(name = "ans_correct")
    private String ansCorrect;

    @OneToMany(mappedBy = "questionExam")
    private List<ExamQuestion> listQuestionExam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private Classroom question_classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject question_subject;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnsA() {
        return ansA;
    }

    public void setAnsA(String ansA) {
        this.ansA = ansA;
    }

    public String getAnsB() {
        return ansB;
    }

    public void setAnsB(String ansB) {
        this.ansB = ansB;
    }

    public String getAnsC() {
        return ansC;
    }

    public void setAnsC(String ansC) {
        this.ansC = ansC;
    }

    public String getAnsD() {
        return ansD;
    }

    public void setAnsD(String ansD) {
        this.ansD = ansD;
    }

    public String getAnsCorrect() {
        return ansCorrect;
    }

    public void setAnsCorrect(String ansCorrect) {
        this.ansCorrect = ansCorrect;
    }

    public List<ExamQuestion> getListQuestionExam() {
        return listQuestionExam;
    }

    public void setListQuestionExam(List<ExamQuestion> listQuestionExam) {
        this.listQuestionExam = listQuestionExam;
    }

    public Classroom getQuestion_classroom() {
        return question_classroom;
    }

    public void setQuestion_classroom(Classroom question_classroom) {
        this.question_classroom = question_classroom;
    }

    public Subject getQuestion_subject() {
        return question_subject;
    }

    public void setQuestion_subject(Subject question_subject) {
        this.question_subject = question_subject;
    }
}
