package com.wru.onthi.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "school")
public class School {
    @Id
    @Column(name ="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="schoolname")
    private String schoolname;

    @Column(name = "code")
    private String code;
    @OneToMany(mappedBy = "school")
    private List<Classroom> listclassrooms;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Classroom> getListclassrooms() {
        return listclassrooms;
    }

    public void setListclassrooms(List<Classroom> listclassrooms) {
        this.listclassrooms = listclassrooms;
    }
}
