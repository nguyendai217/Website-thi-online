package com.wru.onthi.controller.admin;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/result")
public class ResultExamController {

    @GetMapping("/list-result")
    public String getListResultExam(Model model, Principal principal,
                                    String username,String codeExam,
                                    Pageable pageable){

        return "";
    }


}
