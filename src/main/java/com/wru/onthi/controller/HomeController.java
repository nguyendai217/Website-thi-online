package com.wru.onthi.controller;

import com.wru.onthi.entity.Classroom;
import com.wru.onthi.entity.Exam;
import com.wru.onthi.entity.Lesson;
import com.wru.onthi.entity.Subject;
import com.wru.onthi.model.LessonNew;
import com.wru.onthi.repository.ExamRepository;
import com.wru.onthi.services.ClassroomService;
import com.wru.onthi.services.ExamService;
import com.wru.onthi.services.LessonService;
import com.wru.onthi.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.rmi.dgc.Lease;
import java.util.ArrayList;
import java.util.List;


@Controller
public class HomeController {

    @Autowired
    ClassroomService classroomService;

    @Autowired
    LessonService lessonService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    ExamService examService;


    @GetMapping(value = {"/","/home"})
    public String index(Model model){

        // get list class
        List<Classroom> listClass= new ArrayList<>();
        listClass = classroomService.getAllClassroom();
        if(!listClass.isEmpty()){
            model.addAttribute("listClass",listClass);
        }

        List<Classroom> listClassTH= classroomService.listClassBySchool(1);
        model.addAttribute("listClassTH",listClassTH);
        List<Classroom> listClassTHCS= classroomService.listClassBySchool(2);
        model.addAttribute("listClassTHCS",listClassTHCS);
        List<Classroom> listClassTHPT= classroomService.listClassBySchool(3);
        model.addAttribute("listClassTHPT",listClassTHPT);

        //get list lesson new
//        List<LessonNew> listLesson = lessonService.getLessonNew();
//        if(!listLesson.isEmpty()){
//            model.addAttribute("listLesson",listLesson);
//        }

        //get list-subject
        List<Subject> listSubject= new ArrayList<>();
        listSubject= subjectService.getlistSubject().subList(0,8);
        if(!listSubject.isEmpty()){
            model.addAttribute("listSubject",listSubject);
        }

        // get list-exam
        List<Exam> listExam= new ArrayList<>();
        listExam= examService.getListExam().subList(0,3);
        if(!listExam.isEmpty()){
            model.addAttribute("listExam",listExam);
        }

        // get list-lesson
        List<Lesson> lessonList= new ArrayList<>();
        lessonList= lessonService.getListLessonOrderById().subList(0,5);
        if(!lessonList.isEmpty()){
            model.addAttribute("lessonList",lessonList);
        }
        return "index";
    }
}
