package com.wru.onthi.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category_news")
public class CategoryNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "status")
    private Integer status;

    @OneToMany(mappedBy ="categoryNews",cascade = CascadeType.ALL)
    private List<News>listNews;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<News> getListNews() {
        return listNews;
    }

    public void setListNews(List<News> listNews) {
        this.listNews = listNews;
    }
}
