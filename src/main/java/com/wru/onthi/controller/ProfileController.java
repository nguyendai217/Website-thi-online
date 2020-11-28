package com.wru.onthi.controller;

import com.wru.onthi.entity.Classroom;
import com.wru.onthi.entity.User;
import com.wru.onthi.services.ClassroomService;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    ClassroomService classroomService;

    @Autowired
    UserService userService;

    @GetMapping("/user/profile")
    public String getProfile(Model model, Principal principal){
        // get list class menu
        List<Classroom> listClass = classroomService.getAllClassroom();
        if(!listClass.isEmpty()){
            model.addAttribute("listClass",listClass);
        }

        String name = principal.getName();
        User user= userService.findUserByName(name);
        model.addAttribute("us",user);
        return "profile";
    }

}
