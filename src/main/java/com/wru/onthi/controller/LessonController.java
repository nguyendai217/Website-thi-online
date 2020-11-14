package com.wru.onthi.controller;

import com.google.common.base.Strings;
import com.wru.onthi.entity.Classroom;
import com.wru.onthi.entity.Lesson;
import com.wru.onthi.services.ClassroomService;
import com.wru.onthi.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class LessonController {
    @Autowired
    LessonService lessonService;

    @Autowired
    ClassroomService classroomService;


    @GetMapping("/baihoc")
    public String getListLesson(Model model,
                                @RequestParam("class_id") Integer class_id,
                                @RequestParam("subject_id") Integer subject_id){
        // get list class menu
        List<Classroom> listClass = classroomService.getAllClassroom();
        if(!listClass.isEmpty()){
            model.addAttribute("listClass",listClass);
        }
        List<Lesson> getListlessonByClassAndSubject= lessonService.getListLessonByClassAndSubject(class_id,subject_id);

        model.addAttribute("listLessonByClass",getListlessonByClassAndSubject);
        return "lesson/list-lesson";



    }


}
