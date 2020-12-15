package com.wru.onthi.controller;

import com.wru.onthi.entity.Classroom;
import com.wru.onthi.services.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.rmi.MarshalledObject;
import java.security.Principal;
import java.util.List;
import java.util.PrimitiveIterator;

@Controller
public class ContactController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    ClassroomService classroomService;

    @GetMapping("/contact")
    public String contact(Model model, Principal principal){
        // get list class menu
        List<Classroom> listClass = classroomService.getAllClassroom();
        if(!listClass.isEmpty()){
            model.addAttribute("listClass",listClass);
        }
        return "contact";
    }

    @PostMapping("/contact/send")
    public String submitContact(Model model,HttpServletRequest  request){
        List<Classroom> listClass = classroomService.getAllClassroom();
        if(!listClass.isEmpty()){
            model.addAttribute("listClass",listClass);
        }
        String fullname= request.getParameter("fullname");
        String email= request.getParameter("email");
        String phone= request.getParameter("phone");
        String content= request.getParameter("content");

        SimpleMailMessage mailMessage= new SimpleMailMessage();
        mailMessage.setFrom("contact@luyenthi.com");
        mailMessage.setTo("nguyendai2171998@gmail.com");
        String mailSubject= fullname +"Gửi phản hồi";
        String mailContent= "Email người gửi: "+ email+ "\n" +"Số điện thoại :" +phone+"\n";
        mailContent += "Nội dung: "+ content +"\n";

        mailMessage.setSubject(mailSubject);
        mailMessage.setText(mailContent);
        mailSender.send(mailMessage);
        return "message_contact";
    }

}
