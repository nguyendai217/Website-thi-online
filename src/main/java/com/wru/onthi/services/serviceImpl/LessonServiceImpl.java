package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.Lesson;
import com.wru.onthi.repository.LessonRepository;
import com.wru.onthi.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService {
    @Autowired
    LessonRepository lessonRepository;

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
}
