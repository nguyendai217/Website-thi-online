package com.wru.onthi.controller.admin;

import com.google.common.base.Strings;
import com.wru.onthi.entity.Subject;
import com.wru.onthi.entity.User;
import com.wru.onthi.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class HomeControllerAdmin {
    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    ExamService examService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    LessonService lessonService;

    @Autowired
    NewsService newsService;

    @Autowired
    ResultService resultService;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Value("${folder.upload}")
    String foldeUpload;

    @GetMapping("/admin/home")
    public String homeAdmin(Model model, Principal principal){
        String username= principal.getName();
        if(username != null){
            User user= userService.findUserByName(username);
            String email= user.getEmail();
            String image= user.getImage();
            model.addAttribute("image",image);
            model.addAttribute("username", username);
            model.addAttribute("email", email);
        }

        Long totalUser= userService.getCountUser();
        Long totalExam= examService.countExam();
        Long totalSubject= subjectService.countSubject();
        Long totalResult= resultService.countResult();
        Long totalNews= newsService.countNews();
        Long totalLesson = lessonService.countLesson();
        Long totalQuestion= questionService.countQuestion();


        model.addAttribute("user",totalUser);
        model.addAttribute("exam",totalExam);
        model.addAttribute("subject",totalSubject);
        model.addAttribute("result",totalResult);
        model.addAttribute("news",totalNews);
        model.addAttribute("lesson",totalLesson);
        model.addAttribute("question",totalQuestion);
        return "admin/home";
    }

    @GetMapping("/profile")
    public String profileGet(Model model,Principal principal){
      getInfoUser(model,principal);
      String name = principal.getName();
      User user= userService.findUserByName(name);
      model.addAttribute("us",user);
      return "admin/profile";
    }

    @PostMapping("/profile")
    public String profilePost(Model model, Principal principal,
                              RedirectAttributes redir,HttpServletRequest request){

        Integer id =Integer.valueOf(request.getParameter("userId"));
        String fullname= request.getParameter("fullname");
        String phone= request.getParameter("phone");
        String address= request.getParameter("address");
        Integer gender= Integer.valueOf(request.getParameter("gender"));
        String birthday= request.getParameter("birthday");
        getInfoUser(model,principal);

        Optional<User> optionalUser= userService.findById(id);
        User user = optionalUser.get();
        //User us= userService.findUserByName(username);

        user.setFullname(fullname);
        user.setAddress(address);
        user.setPhone(phone);
        user.setGender(gender);
        user.setUpdateDate(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            if(birthday!= ""){
                Date dateBirthday= sdf.parse(birthday);
                user.setBirthday(dateBirthday);
            }
            else {
                user.setBirthday(null);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            userService.updateUser(user);
        }
        catch (Exception e){
            redir.addFlashAttribute("error","Cập nhật thông tin thất bại");
        }
        redir.addFlashAttribute("success","Cập nhật thông tin thành công.");

        return "redirect:/profile";
    }

    @PostMapping("/profile/changepass")
    public String changePass(Model model, Principal principal,
                             @RequestParam(value = "password_old") String oldPassword,
                             @RequestParam(value = "password1") String pass,
                             @RequestParam(value = "password2") String pass2){
        getInfoUser(model,principal);
        String username= principal.getName();
        User user = userService.findUserByName(username);
        if(!Strings.isNullOrEmpty(pass) && !Strings.isNullOrEmpty(pass2)){
            if(pass.equals(pass2)){
                String userPass= user.getPassword();
                boolean checkpass= encoder.matches(oldPassword,userPass);
                if(checkpass== true){
                    String encodePass= encoder.encode(pass);
                    user.setPassword(encodePass);
                    try {
                        userService.updateUser(user);
                        model.addAttribute("success","Thay đổi mật khẩu thành công.");
                    }catch (Exception e){
                        model.addAttribute("error","Thay đổi mật khẩu thất bại");
                    }
                }else {
                    model.addAttribute("error","Mật khẩu cũ không đúng, vui lòng nhập lại");
                }
            } else {
                model.addAttribute("error","Mật khẩu không trùng nhau, vui lòng nhập lại");
                
            }
        }
        model.addAttribute("us",user);
        return "admin/profile";
    }

    @PostMapping("/profile/updateImage")
    public String updateImage(@RequestParam("fileImage") MultipartFile multipartFile,
                              @RequestParam("userId") Integer id,RedirectAttributes redir) {
        String imgname= null;
        UploadImageController uploadImageController= new UploadImageController();
        if(multipartFile != null){
            imgname= uploadImageController.getImageName(multipartFile);
        }
        try {
            Optional<User> optional= userService.findById(id);
            User user= optional.get();
            user.setImage(imgname);
            userService.updateUser(user);
            uploadImageController.uploadImage(multipartFile,imgname,foldeUpload,"user");
            redir.addFlashAttribute("success","Thay đổi ảnh thành công.");
        } catch (IOException e) {
            e.printStackTrace();
            redir.addFlashAttribute("success","Thay đổi ảnh thất bại.");
        }
        return "redirect:/profile";
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
