package com.wru.onthi.controller.admin;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.wru.onthi.entity.User;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.PrimitiveIterator;

@Controller
public class ThongKeController {
    @Autowired
    UserService userService;

    @GetMapping("/thongke")
    public String thongkeDiem(Principal principal, Model model){
        getInfoUser(model,principal);
        return "admin/thongke";
    }

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
