package com.wru.onthi.entity;

import javax.persistence.*;
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

    @Column(name = "code_exam")
    private String codeExam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject exam_subject;

    @OneToMany(mappedBy = "examQuestion")
    private List<Question> listQuestion;

    @Column(name = "time_out")
    private String timeOut;

}
