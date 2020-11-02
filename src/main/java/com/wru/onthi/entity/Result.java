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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userResult;

}
