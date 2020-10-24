package com.wru.onthi.controller.admin;

import com.wru.onthi.entity.Role;
import com.wru.onthi.entity.User;
import com.wru.onthi.repository.RoleRepository;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;


@Controller
@RequestMapping("/admin")
public class UserMangerController {
    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder encoderPass;

    @Autowired
    RoleRepository roleRepository;


    @GetMapping("/user/list-user")
    public String getAllUser(Model model, Principal principal, Pageable pageable){
        // get info user login
        getInfoUser(model,principal);

        // pageable list user
        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable newPageAble = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "username"));
        Page<User> pageUser = userService.getAllUser(newPageAble);

        int totalItem = (int) pageUser.getTotalElements();
        int itemPerPage= pageSize * (pageNumber+1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }
        model.addAttribute("pageInfo",pageUser);
        model.addAttribute("total",totalItem);
        model.addAttribute("itemPerPage",itemPerPage);

        return "admin/user/list-user";
    }

    @GetMapping("/user/search-user")
    public String searchUser(Model model, Principal principal, Pageable pageable,
                             @RequestParam(value = "username", defaultValue = "") String username,
                             @RequestParam(value = "email",defaultValue = "") String email,
                             @RequestParam(value = "phone", defaultValue = "") String phone){
        getInfoUser(model,principal);
        //get list user-search
//        List<User> listUser= new ArrayList<>();
//        listUser= userService.searchUser(username,email,phone);
        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable newPageAble = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "username"));
        Page<User> pageUser = userService.searchUser(username,email,phone,newPageAble);

        int totalItem = (int) pageUser.getTotalElements();
        int itemPerPage= pageSize * (pageNumber +1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }
        model.addAttribute("pageInfo",pageUser);
        model.addAttribute("total",totalItem);
        model.addAttribute("itemPerPage",itemPerPage);

        return "admin/user/list-user";
    }

    @GetMapping("/user/detail/{id}")
    public String detailUser(Model model, Principal principal,@PathVariable(value = "id") Integer id){
        getInfoUser(model,principal);

        // get info User by Id
        Optional<User> user= userService.findById(id);
        model.addAttribute("us", user.get());
        return "admin/user/detail-user";
    }

    @PostMapping("/user/add-user")
    public String addUserGet(Model model, Principal principal, RedirectAttributes redir,
                             @RequestParam(value = "username") String username,
                             @RequestParam(value = "fullname") String fullname,
                             @RequestParam(value = "email") String email,
                             @RequestParam(value = "password") String password,
                             @RequestParam(value = "phone") String phone,
                             @RequestParam(value = "address") String address,
                             @RequestParam(value = "gender") Integer gender,
                             @RequestParam(value = "birthday") String birthday,
                             @RequestParam(value = "role") Integer role){
        getInfoUser(model,principal);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        //check email is exist
        User checkUser= userService.findByUsernameOrEmail(username,email);
        if(checkUser != null){
            model.addAttribute("error","Username hoặc email đã tồn tại.");
            return "admin/user/add-user";
        }
        else {
            User user= new User();
            String roleName="";
            user.setUsername(username);
            user.setFullname(fullname);
            user.setEmail(email);
            user.setPhone(phone);
            user.setPassword(encoderPass.encode(password));
            user.setStatus(1);
            user.setCreateDate(new Date());
            user.setAddress(address);
            user.setGender(gender);
            //user.setBirthday(birthday);
            try {
                if(birthday!= null){
                    Date dateBirthday= sdf.parse(birthday);
                    user.setBirthday(dateBirthday);
                }
                else {
                    user.setBirthday(null);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(role==1){
               roleName = "ADMIN";
            }else if(role==0){
                roleName="USER";
            }else if(role==2){
                roleName="MANAGER";
            }
            Role roleUser = roleRepository.findByRole(roleName);
            user.setRoles(Arrays.asList(roleUser));
            userService.createUser(user);

            redir.addFlashAttribute("success","Thêm user thành công.");
            return "redirect:/admin/user/list-user";
        }
    }

    @GetMapping("/user/add-user")
    public String addUserPost(Model model, Principal principal){
        getInfoUser(model,principal);
        return "admin/user/add-user";
    }

    @GetMapping("/user/update/{id}")
    public String updateUserGet(Model model,Principal principal, @PathVariable("id") Integer id){
        // get info User by Id
        Optional<User> user= userService.findById(id);
        model.addAttribute("user", user.get());
        getInfoUser(model,principal);
        return "admin/user/update-user";
    }

    @PostMapping("/user/update-user")
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
            String image= user.getImage();
            model.addAttribute("image",image);
            model.addAttribute("username", username);
            model.addAttribute("email", email);
        }
    }
}
