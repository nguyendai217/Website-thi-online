package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.Lesson;
import com.wru.onthi.model.LessonNew;
import com.wru.onthi.repository.LessonRepository;
import com.wru.onthi.services.LessonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService {
    @Autowired
    LessonRepository lessonRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void createLesson(Lesson lesson) {
        lessonRepository.save(lesson);
    }

    @Override
    public Lesson updateLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public void deleteLesson(Lesson lesson) {
        lessonRepository.delete(lesson);
    }

    @Override
    public List<Lesson> getListLesson() {
        return lessonRepository.findAll();
    }

    @Override
    public Optional<Lesson> findByLessonId(Integer id) {
        return lessonRepository.findById(id);
    }

    @Override
    public List<Lesson> findByName(String name) {
        return lessonRepository.findByLessonName(name);
    }

    @Override
    public Page<Lesson> getAllLesson(Pageable pageable) {
        return lessonRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public List<LessonNew> getLessonNew() {
        List<LessonNew> list= new ArrayList<>();
        Query query= entityManager.createNativeQuery(
                "SELECT ls.lessonname, sb.name " +
                "FROM lesson ls " +
                "join subject sb on ls.subject_id= sb.id " +
                "order by ls.id desc");

        query.setMaxResults(3);
        List<Object[]> objects = query.getResultList();
        for (Object[] record : objects) {
            LessonNew lessonNew= new LessonNew();
            lessonNew.setLessonName(StringUtils.defaultIfEmpty((String) record[0], StringUtils.EMPTY));
            lessonNew.setSubjectName(StringUtils.defaultIfEmpty((String) record[1], StringUtils.EMPTY));
            list.add(lessonNew);
        }
        return list;
    }

    @Override
    public List<Lesson> getListLessonOrderById() {
        return lessonRepository.getlistLessonOrderByID();
    }
}
