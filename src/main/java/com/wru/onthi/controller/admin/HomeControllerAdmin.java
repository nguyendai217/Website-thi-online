package com.wru.onthi.controller.admin;

import com.wru.onthi.entity.Role;
import com.wru.onthi.entity.User;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class HomeControllerAdmin {
    @Autowired
    UserService userService;

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
    public String profilePost(Model model, Principal principal, RedirectAttributes red,
                              @RequestParam(value = "userId",defaultValue = "") Integer id,
                              @RequestParam(value = "username",defaultValue = "") String username,
                              @RequestParam(value = "fullname",defaultValue = "") String fullname,
                              @RequestParam(value = "phone",defaultValue = "") String phone,
                              @RequestParam(value = "address",defaultValue = "") String address,
                              @RequestParam(value = "gender",defaultValue = "") Integer gender,
                              @RequestParam(value = "birthday",defaultValue = "") String birthday){

        getInfoUser(model,principal);

//        Optional<User> optionalUser= userService.findById(id);
//        User user = optionalUser.get();
        User user= userService.findUserByName(username);
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
            red.addFlashAttribute("error","Cập nhật thông tin thất bại");
        }
        red.addFlashAttribute("success","Cập nhật thông tin thành công.");
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
