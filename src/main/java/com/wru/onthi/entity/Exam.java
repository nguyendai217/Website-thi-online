package com.wru.onthi.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "exam")
public class Exam {

//    1 môn hoc có nhiều đề thi
//    1 dè thi thuojc 1 mon hoc
//    1 de thi có nhieu câu hỏi

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // mã đề thi
    @Column(name = "code_exam")
    private String codeExam;

    // tieu de de thi
    @Column(name = "title")
    private String title;

    // noi dung de thi
    @Column(name = "content")
    private String content;

    @Column(name = "create_by")
    private String createBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject exam_subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private Classroom exam_classroom;

    @Column(name = "total_question")
    private Integer totalQuestion;

    @Column(name = "time_out")
    private Integer timeOut;

    @Column(name = "views")
    private Integer views;

    @Column(name = "create_date")
    private Date createDate;

    @OneToMany(mappedBy = "examQuestion")
    private List<ExamQuestion> listExamQuestion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodeExam() {
        return codeExam;
    }

    public void setCodeExam(String codeExam) {
        this.codeExam = codeExam;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Subject getExam_subject() {
        return exam_subject;
    }

    public void setExam_subject(Subject exam_subject) {
        this.exam_subject = exam_subject;
    }


    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(Integer totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public Classroom getExam_classroom() {
        return exam_classroom;
    }

    public void setExam_classroom(Classroom exam_classroom) {
        this.exam_classroom = exam_classroom;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public List<ExamQuestion> getListExamQuestion() {
        return listExamQuestion;
    }

    public void setListExamQuestion(List<ExamQuestion> listExamQuestion) {
        this.listExamQuestion = listExamQuestion;
    }
}
