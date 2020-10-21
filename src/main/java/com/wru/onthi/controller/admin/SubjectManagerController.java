package com.wru.onthi.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/subject")
public class SubjectManagerController {
    @GetMapping("/list-subject")
    public String listSubject(){
        return "admin/subject/list-subject";
    }

}
