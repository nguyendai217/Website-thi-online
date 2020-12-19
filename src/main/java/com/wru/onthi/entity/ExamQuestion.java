package com.wru.onthi.entity;

import javax.persistence.*;

@Entity
@Table(name = "exam_question")
public class ExamQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="exam_id" )
    private Exam examQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="question_id" )
    private Question questionExam;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Exam getExamQuestion() {
        return examQuestion;
    }

    public void setExamQuestion(Exam examQuestion) {
        this.examQuestion = examQuestion;
    }

    public Question getQuestionExam() {
        return questionExam;
    }

    public void setQuestionExam(Question questionExam) {
        this.questionExam = questionExam;
    }
}
