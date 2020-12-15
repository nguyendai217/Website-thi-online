package com.wru.onthi.controller;

import com.google.common.base.Strings;
import com.wru.onthi.controller.admin.UploadImageController;
import com.wru.onthi.entity.Classroom;
import com.wru.onthi.entity.User;
import com.wru.onthi.services.ClassroomService;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class ProfileController {

    @Autowired
    ClassroomService classroomService;

    @Autowired
    UserService userService;

    @Value("${folder.upload}")
    private String folderUpload;

    @Autowired
    BCryptPasswordEncoder encoder;

    @GetMapping("/user/profile")
    public String getProfile(Model model, Principal principal){
        // get list class menu
        List<Classroom> listClass = classroomService.getAllClassroom();
        if(!listClass.isEmpty()){
            model.addAttribute("listClass",listClass);
        }

        String name = principal.getName();
        User user= userService.findUserByName(name);
        model.addAttribute("us",user);
        return "profile";
    }

    @PostMapping("/profile/update-user")
    public String updateProfilePost(RedirectAttributes redir, HttpServletRequest request){
        Integer id =Integer.valueOf(request.getParameter("userId"));
        String fullname= request.getParameter("fullname");
        String phone= request.getParameter("phone");
        String address= request.getParameter("address");
        Integer gender= Integer.valueOf(request.getParameter("gender"));
        String birthday= request.getParameter("birthday");

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

        return "redirect:/user/profile";
    }

    @PostMapping("/profile/update-image-user")
    public String updateImageNews(Principal principal,RedirectAttributes redr,
                                  @RequestParam("image") MultipartFile multipartFile,
                                  HttpServletRequest request){

        UploadImageController uploadImageController= new UploadImageController();
        String imgname= uploadImageController.getImageName(multipartFile);

        Integer userId= Integer.valueOf(request.getParameter("userId"));
        Optional<User> optionalUser= userService.findById(userId);
        User user= optionalUser.get();
        try {
            user.setImage(imgname);
            user.setUpdateDate(new Date());
            user.setUpdateBy(principal.getName());
            userService.updateUser(user);
            uploadImageController.uploadImage(multipartFile,imgname,folderUpload,"user");
            redr.addFlashAttribute("success","Update hình ảnh thành công");
        }catch (Exception e){
            e.printStackTrace();
            redr.addFlashAttribute("error","update hình ảnh thất bại");
        }
        return "redirect:/user/profile";
    }

    @PostMapping("/profile/changepassword-user")
    public String changePass(RedirectAttributes redr, Principal principal,
                             @RequestParam(value = "password_old") String oldPassword,
                             @RequestParam(value = "password1") String pass,
                             @RequestParam(value = "password2") String pass2){
        String username= principal.getName();
        User user = userService.findUserByName(username);
        if(!Strings.isNullOrEmpty(pass) && !Strings.isNullOrEmpty(pass2)){
            if(pass.equals(pass2)){
                String userPass= user.getPassword();
                boolean checkpass= encoder.matches(oldPassword,userPass);
                if(checkpass == true){
                    String encodePass= encoder.encode(pass);
                    user.setPassword(encodePass);
                    try {
                        userService.updateUser(user);
                        redr.addFlashAttribute("success","Thay đổi mật khẩu thành công.");
                    }catch (Exception e){
                        redr.addFlashAttribute("error","Thay đổi mật khẩu thất bại");
                    }
                }else {
                    redr.addFlashAttribute("error","Mật khẩu cũ không đúng, vui lòng nhập lại");
                }
            } else {
                redr.addFlashAttribute("error","Mật khẩu không trùng nhau, vui lòng nhập lại");
            }
        }
        return "redirect:/user/profile";
    }



}
