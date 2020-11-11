package com.wru.onthi.controller;

import com.wru.onthi.entity.Classroom;
import com.wru.onthi.services.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SubjectController {

    @Autowired
    ClassroomService classroomService;

    @GetMapping("/monhoc")
    public String listSubject(Model model, @RequestParam("class_id") Integer id){
        // get list class menu
        List<Classroom> listClass= new ArrayList<>();
        listClass = classroomService.getAllClassroom();
        if(!listClass.isEmpty()){
            model.addAttribute("listClass",listClass);
        }
        return "subject/list-subject";
    }
}
