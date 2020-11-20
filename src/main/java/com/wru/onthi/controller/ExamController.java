package com.wru.onthi.controller;

import com.wru.onthi.entity.Classroom;
import com.wru.onthi.entity.Exam;
import com.wru.onthi.entity.Lesson;
import com.wru.onthi.services.ClassroomService;
import com.wru.onthi.services.ExamService;
import com.wru.onthi.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class ExamController {

    @Autowired
    ClassroomService classroomService;

    @Autowired
    ExamService examService;

    @Autowired
    LessonService lessonService;


    @GetMapping("/kiemtra")
    public String getAllClass(Model model, Principal principal){
        //get ListClass
        List<Classroom> listClass= classroomService.getAllClassroom();
        model.addAttribute("listClass",listClass);
        return "exam/list-class";
    }

    private void genDefault(Model model){
        // get list class menu
        List<Classroom> listClass = classroomService.getAllClassroom();
        if(!listClass.isEmpty()){
            model.addAttribute("listClass",listClass);
        }
        // list lesson views
        List<Lesson> listLesson =lessonService.getListLessonOrderByViews().subList(0,5);
        model.addAttribute("listLesson",listLesson);

        //list exam views
        List<Exam> listExam =examService.getListExamOrderByViews().subList(0,5);
        model.addAttribute("listExam",listExam);
    }

    @GetMapping("/lophoc/list-class")
    public String getClassBySubject(Model model, Principal principal, @RequestParam("subjectId") Integer subjectId){
        genDefault(model);
        // get list class by subject

        List<Classroom> getListClassBySubject= classroomService.listClassBySubject(subjectId);
        model.addAttribute("listClass",getListClassBySubject);
        return "exam/list-class-bySubject";
    }

}
