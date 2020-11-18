package com.wru.onthi.controller.admin;

import com.wru.onthi.entity.*;
import com.wru.onthi.services.ClassroomService;
import com.wru.onthi.services.ExamService;
import com.wru.onthi.services.SubjectService;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/exam")
public class ExamControllerAdmin {
    @Autowired
    UserService userService;

    @Autowired
    ExamService examService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    ClassroomService classroomService;

    @GetMapping("/list-exam")
    public String getListExam(Model model, Principal principal,Pageable pageable){
        getInfoUser(model,principal);

        //get AllClassroom
        List<Classroom> lístClass= classroomService.getAllClassroom();
        model.addAttribute("listClass",lístClass);

        //get list Subject
        List<Subject> listSubject= subjectService.getlistSubject();
        model.addAttribute("listSubject", listSubject);

        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize);
        Page<Exam> pageExam = examService.getAllListExam(pageItem);

        int totalItem = (int) pageExam.getTotalElements();
        int itemPerPage= pageSize * (pageNumber+1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }

        if(pageExam == null){
            model.addAttribute("error","Danh sách bài thi trống.");
            return "admin/exam/list-exam";
        } else {
            model.addAttribute("pageInfo", pageExam);
            model.addAttribute("total",totalItem);
            model.addAttribute("itemPerPage",itemPerPage);
            model.addAttribute("path","/exam/list-exam");
            return "admin/exam/list-exam";
        }

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
