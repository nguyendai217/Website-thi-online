package com.wru.onthi.controller;

import com.wru.onthi.entity.Classroom;
import com.wru.onthi.entity.Subject;
import com.wru.onthi.services.ClassroomService;
import com.wru.onthi.services.SubjectService;
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

    @GetMapping("/monhoc")
    public String listSubject(Model model, @RequestParam("class_id") Integer id){
        // get list class menu
        List<Classroom> listClass= new ArrayList<>();
        listClass = classroomService.getAllClassroom();
        if(!listClass.isEmpty()){
            model.addAttribute("listClass",listClass);
        }

        List<Subject> listSubject= subjectService.getListSubjectByClass(id);
        model.addAttribute("listSubject",listSubject);
        Optional<Classroom> optional= classroomService.findById(id);
        Classroom classroom= optional.get();
        String className= classroom.getClassname();
        model.addAttribute("className",className);
        return "subject/list-subject";
    }
}
