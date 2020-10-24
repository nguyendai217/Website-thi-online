package com.wru.onthi.controller.admin;

import com.wru.onthi.entity.Subject;
import com.wru.onthi.entity.User;
import com.wru.onthi.services.SubjectService;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/subject")
public class SubjectManagerController {

    @Autowired
    UserService userService;

    @Autowired
    SubjectService subjectService;

    @GetMapping("/list-subject")
    public String listSubject(Model model, Principal principal,Pageable pageable){
        getInfoUser(model,principal);

        // pageable list user
        int pageNumber = pageable.getPageNumber();
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, 5);

        Page<Subject> pageSubject = subjectService.getAllSubject(pageItem);
        model.addAttribute("pageInfo",pageSubject);
        return "admin/subject/list-subject";
    }

    @GetMapping("/add-subject")
    public String addSubjectGet(Model model,Principal principal){
        //get info user sildebar
        getInfoUser(model,principal);

        return "admin/subject/add-subject";
    }

    // get info user login
    private void getInfoUser(Model model,Principal principal){
        String username= principal.getName();
        if(username != null){
            User user= userService.findUserByName(username);
            String email= user.getEmail();
            String image= user.getImage();
            model.addAttribute("image",image);
            model.addAttribute("username", username);
            model.addAttribute("email", email);
        }
    }

}
