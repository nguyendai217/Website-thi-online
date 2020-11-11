package com.wru.onthi.controller;

import com.wru.onthi.entity.Classroom;
import com.wru.onthi.entity.Lesson;
import com.wru.onthi.model.LessonNew;
import com.wru.onthi.services.ClassroomService;
import com.wru.onthi.services.LessonService;
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

    @GetMapping(value = {"/","/home"})
    public String index(Model model){

        // get list class
        List<Classroom> lists= new ArrayList<>();
        lists = classroomService.getAllClassroom();
        if(!lists.isEmpty()){
            model.addAttribute("listClass",lists);
        }
        //get list lesson new
        List<LessonNew> listLesson = lessonService.getLessonNew();
        if(!listLesson.isEmpty()){
            model.addAttribute("listLesson",listLesson);
        }
        return "index";
    }
}
