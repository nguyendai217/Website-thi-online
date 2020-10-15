package com.wru.onthi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ContactController {
    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }

    @PostMapping("/contact")
    public String submitContact(HttpServletRequest  request){
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
