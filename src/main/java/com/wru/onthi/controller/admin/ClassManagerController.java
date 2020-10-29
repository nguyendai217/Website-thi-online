package com.wru.onthi.controller.admin;

import com.wru.onthi.entity.Classroom;
import com.wru.onthi.entity.School;
import com.wru.onthi.entity.User;
import com.wru.onthi.services.ClassroomService;
import com.wru.onthi.services.SchoolService;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ClassManagerController {

    @Autowired
    UserService userService;

    @Autowired
    ClassroomService classroomService;

    @Autowired
    SchoolService schoolService;

    @GetMapping("/class/list-class")
    public String listClass(Model model, Principal principal, Pageable pageable){
       getInfoUser(model,principal);

        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        Page<Classroom> pageClass = classroomService.getAllClass(pageItem);

        int totalItem = (int) pageClass.getTotalElements();
        int itemPerPage= pageSize * (pageNumber+1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }
        model.addAttribute("pageInfo",pageClass);
        model.addAttribute("total",totalItem);
        model.addAttribute("itemPerPage",itemPerPage);
       return "admin/classroom/list-class";
    }

    @GetMapping("/class/add-class")
    public String addClass(Model model, Principal principal){
        getInfoUser(model,principal);

        List<School> listSchool= new ArrayList<>();
        listSchool= schoolService.getListSchool();

        model.addAttribute("listSchool",listSchool);

        return "admin/classroom/add-class";
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
