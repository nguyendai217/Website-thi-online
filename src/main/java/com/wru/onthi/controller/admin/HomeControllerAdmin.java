package com.wru.onthi.controller.admin;

import com.wru.onthi.entity.User;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeControllerAdmin {
    @Autowired
    UserService userService;

    @GetMapping("/admin/home")
    public String homeAdmin(Model model, Principal principal){
        String username= principal.getName();
        if(username != null){
            User user1= userService.findUserByName(username);
            String email= user1.getEmail();
            model.addAttribute("username", username);
            model.addAttribute("email", email);
        }

        return "admin/home";
    }
}
