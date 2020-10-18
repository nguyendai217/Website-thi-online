package com.wru.onthi.controller;

import com.google.common.base.Strings;
import com.wru.onthi.entity.Lesson;
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
@RequestMapping("/lesson")
public class LessonController {
    @Autowired
    LessonService lessonService;

    @GetMapping("/list-lesson")
    public String getListLesson(Model model) {
        // Get List Lesson
        List<Lesson> listLesson = new ArrayList<>();
        listLesson = lessonService.getListLesson();
        return "";
    }

    @PostMapping("/list-lesson")
    public String postLesson(Model model, @RequestParam(value = "name") String name) {
        if (!Strings.isNullOrEmpty(name)) {
            List<Lesson> listLesson = new ArrayList<>();
            listLesson = lessonService.findByName(name);
        }
        return "";
    }

    @GetMapping("/add-lesson")
    public String addLesson(){
        return "";
    }

//    @PostMapping("/add-lesson")
//    public String addLessionPost(Model model)

    @PostMapping("/delete-lesson")
    public String deleteLesson(@RequestParam(value = "id") Integer id){
        if(id != null){
            Optional<Lesson> lesson= lessonService.findByLessonId(id);
            if(lesson != null){
                // delete lesson
                lessonService.deleteLesson(lesson.get());
            }
            else {
                return "Lesson not exist!";
            }
        }
        else{
            return "ID not exist";
        }
        return "";
    }


}
