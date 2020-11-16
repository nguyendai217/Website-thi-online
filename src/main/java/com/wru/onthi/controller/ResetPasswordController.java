package com.wru.onthi.controller;

import com.wru.onthi.entity.User;
import com.wru.onthi.repository.UserRepository;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResetPasswordController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/reset_password")
    public String getResetPass(){
        return "resetpassword";
    }

    @PostMapping("/reset_password")
    public String repassword(Model model, @RequestParam(value = "email") String email) {

        // check email exist
        if (userRepository.findByEmail(email) == null) {
            String error="Email không tồn tại !";
            model.addAttribute("error",error);
        } else {
            User user = userRepository.getUserByEmail(email);
            String newpass = String.valueOf(getRandomNumber(100000, 999999));
            user.setPassword(passwordEncoder.encode(newpass));
            userService.updateUser(user);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("contact@luyenthi.com");
            mailMessage.setTo(email);
            String mailSubject = "Cập nhật mật khẩu luyenthi365.com";
            String mailContent = "Password đăng nhập mới của bạn là : " + newpass + "\n";
            mailContent += "Vui lòng đổi mật khẩu khi đăng nhập thành công." +"\n";
            mailContent+="Đăng nhập ngay : luyenthi365.info/login";
            mailMessage.setSubject(mailSubject);
            mailMessage.setText(mailContent);
            mailSender.send(mailMessage);
            String success= "Thay đổi mật khẩu thành công, vui lòng truy cập vào email để lấy thông tin.";
            model.addAttribute("success",success);
        }
        return "resetpassword";
    }
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
