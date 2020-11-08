package com.wru.onthi.entity;

import org.hibernate.annotations.CollectionId;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "classroom")
public class Classroom {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "code")
    private String code;
    @Column(name = "classname")
    private String classname;
    @Column(name = "image")
    private String image;
    @Column(name = "status")
    private Integer status;

    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "classrooms")
    private List<Subject> subjects;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
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

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
