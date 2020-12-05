package com.wru.onthi.controller;

import com.wru.onthi.entity.*;
import com.wru.onthi.services.*;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class SubjectController {

    @Autowired
    ClassroomService classroomService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    LessonService lessonService;

    @Autowired
    ExamService examService;

    @Autowired
    NewsService newsService;

    @GetMapping("/monhoc")
    public String listSubject(Model model, @RequestParam("class_id") Integer id){
        // get list class menu
        List<Classroom> listClass = classroomService.getAllClassroom();
        if(!listClass.isEmpty()){
            model.addAttribute("listClass",listClass);
        }

        List<Subject> listSubject= subjectService.getListSubjectByClass(id);
        model.addAttribute("listSubject",listSubject);
        Optional<Classroom> optional= classroomService.findById(id);
        Classroom classroom= optional.get();
        String className= classroom.getClassname();
        model.addAttribute("className",className);
        model.addAttribute("classId",id);

        List<Lesson> lessonByClass= lessonService.getListLessonByClass(id).subList(0,4);
        model.addAttribute("lesson", lessonByClass);
        // list lesson views
        List<Lesson> listLesson =lessonService.getListLessonOrderByViews().subList(0,5);
        model.addAttribute("listLesson",listLesson);

        //list exam views
        List<Exam> listExam =examService.getListExamOrderByViews().subList(0,5);
        model.addAttribute("listExam",listExam);

        List<News> listnewStudy= newsService.getNewsStudy().subList(0,4) ;
        model.addAttribute("listnewStudy",listnewStudy);

        return "subject/list-subject";
    }


}
