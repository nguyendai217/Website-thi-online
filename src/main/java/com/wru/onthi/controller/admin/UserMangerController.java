package com.wru.onthi.controller.admin;

import com.wru.onthi.entity.User;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;


@Controller
@RequestMapping("/admin")
public class UserMangerController {
    @Autowired
    UserService userService;

    @GetMapping("/user/list-user")
    public String getAllUser(Model model, Principal principal, Pageable pageable){
        // get info user login
        getInfoUser(model,principal);

        // pageable list user
        int pageNumber = pageable.getPageNumber();
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable newPageAble = PageRequest.of(pageNumber, 6, Sort.by(Sort.Direction.ASC, "username"));

        Page<User> pageUser = userService.getAllUser(newPageAble);
        model.addAttribute("pageInfo",pageUser);
        for (User use: pageUser) {
            use.getRoles().get(0).getRole();
        }
        return "admin/user/list-user";
    }

    @GetMapping("/user/detail/{id}")
    public String detailUser(Model model, Principal principal,@PathVariable(value = "id") Integer id){
        getInfoUser(model,principal);

        // get info User by Id
        Optional<User> user= userService.findById(id);
        model.addAttribute("us", user);
        return "admin/user/detail-user";
    }

    @GetMapping("/add-user")
    public String addUserGet(Model model, Principal principal){
        getInfoUser(model,principal);

        return "admin/user/add-user";
    }

    @PostMapping("/add-user")
    public String addUserPost(Model model, Principal principal){
        getInfoUser(model,principal);

        return "admin/user/add-user";
    }

    @GetMapping("/user/update/{id}")
    public String updateUserGet(Model model,Principal principal, @PathVariable("id") Integer id){
        // get info User by Id
        Optional<User> user= userService.findById(id);
        model.addAttribute("user", user);
        getInfoUser(model,principal);
        return "admin/user/update-user";
    }
    @PostMapping("/update-user")
    public String updateUserPost(Model model,Principal principal){
        getInfoUser(model,principal);

        return "admin/user/update-user";
    }

    // get info user login
    private void getInfoUser(Model model,Principal principal){
        String username= principal.getName();
        if(username != null){
            User user= userService.findUserByName(username);
            String email= user.getEmail();
            model.addAttribute("username", username);
            model.addAttribute("email", email);
        }
    }
}
