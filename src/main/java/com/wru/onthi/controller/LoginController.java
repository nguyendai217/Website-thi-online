package com.wru.onthi.controller;

import com.google.common.base.Strings;
import com.wru.onthi.entity.Role;
import com.wru.onthi.entity.User;
import com.wru.onthi.repository.RoleRepository;
import com.wru.onthi.repository.UserRepository;
import com.wru.onthi.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Date;

@Controller
public class LoginController {
    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/signin")
    public String signIn(Model model,
                         @RequestParam(value = "fullname") String fullname,
                         @RequestParam(value = "email") String email,
                         @RequestParam(value = "password") String password,
                         @RequestParam(value = "phone") String phone) {

        if (!Strings.isNullOrEmpty(fullname) && !Strings.isNullOrEmpty(email)
                && !Strings.isNullOrEmpty(password) && !Strings.isNullOrEmpty(phone)) {

            if (userRepository.findByEmail(email) == null) {
                User user = new User();
                Role role = roleRepository.findByRole("ADMIN");
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode(password));
                user.setFullname(fullname);
                user.setPhone(phone);
                user.setCreateDate(new Date());
                user.setStatus(1);
                user.setRoles(Arrays.asList(role));
                userService.createUser(user);
                model.addAttribute("success", "Đăng kí thành công, vui lòng đăng nhập lại!");
                model.addAttribute("email", email);
            } else {
                LOG.error("email is exist !");
                model.addAttribute("error", "Email đã tồn tại, vui lòng đăng kí email khác!");
            }
        }
        return "login";
    }




}
