package com.wru.onthi.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subject")
public class Subject {
    @Id
    @Column(name ="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name")
    private String name;
    @Column(name = "code")
    private String code;
    @Column(name = "image")
    private String image;
    @Column(name = "status")
    private Integer status;

    @ManyToMany(fetch =FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "subject_class",joinColumns = {
            @JoinColumn(name = "subject_id",nullable = false)},
            inverseJoinColumns ={@JoinColumn(name = "class_id",nullable = false)})
    private List<Classroom> classrooms;

    @OneToMany(mappedBy = "subject",cascade = CascadeType.ALL)
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "exam_subject",cascade = CascadeType.ALL)
    private List<Exam> exams;

    @OneToMany(mappedBy = "question_subject",cascade = CascadeType.ALL)
    private List<Question> lisQuestion;

    public List<Question> getLisQuestion() {
        return lisQuestion;
    }

    public void setLisQuestion(List<Question> lisQuestion) {
        this.lisQuestion = lisQuestion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Classroom> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }
}
