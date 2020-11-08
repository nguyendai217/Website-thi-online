package com.wru.onthi.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.print.Pageable;
import java.security.Principal;

@Controller
@RequestMapping("/question")
public class QuestionController {

    @GetMapping("/list-question")
    public String getAllQuestion(Model model, Principal principal, Pageable pageable){
        return "";
    }

}
