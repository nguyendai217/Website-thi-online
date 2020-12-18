package com.wru.onthi.controller.admin;

import com.sun.java.swing.plaf.windows.WindowsTextAreaUI;
import com.wru.onthi.entity.News;
import com.wru.onthi.entity.Role;
import com.wru.onthi.entity.User;
import com.wru.onthi.repository.RoleRepository;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
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
@RequestMapping("/admin")
public class UserMangerController {
    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder encoderPass;

    @Autowired
    RoleRepository roleRepository;

    @Value("${folder.upload}")
    private String folderUpload;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    @GetMapping("/user/list-user")
    public String getAllUser(Model model, Principal principal, Pageable pageable,
                             String username,String phone){
        // pageable list user
        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<User> pageUser= null;
        if((username== null || username=="") && (phone== null || phone=="")){
            pageUser = userService.getAllUser(pageItem);
        }else {
            pageUser= userService.searchUser(username,phone,pageItem);
            model.addAttribute("us",username);
            model.addAttribute("ph",phone);
        }
        // get info user login
        getInfoUser(model,principal);
        int totalItem = (int) pageUser.getTotalElements();
        int itemPerPage= pageSize * (pageNumber+1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }
        model.addAttribute("pageInfo",pageUser);
        model.addAttribute("total",totalItem);
        model.addAttribute("itemPerPage",itemPerPage);
        model.addAttribute("path","/admin/user/list-user");
        if(username==null){
            username="";
        }
        if(phone== null){
            phone= "";
        }
        model.addAttribute("condition",("&username="+username+"&phone="+phone));
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

    @GetMapping("/user/add-user")
    public String addUserPost(Model model, Principal principal){
        getInfoUser(model,principal);
        return "admin/user/add-user";
    }

    @PostMapping("/user/add-user")
    public String addUserGet(Model model, Principal principal,
                             RedirectAttributes redir,HttpServletRequest request,
                             @RequestParam("image") MultipartFile multipartFile) throws IOException {
        getInfoUser(model, principal);

        String username = request.getParameter("username");
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        Integer gender = Integer.valueOf(request.getParameter("gender"));
        String birthday = request.getParameter("birthday");
        Integer role =Integer.valueOf(request.getParameter("role"));

        String imgname= null;
        if(!multipartFile.isEmpty()){
            UploadImageController uploadImageController= new UploadImageController();
            imgname= uploadImageController.getImageName(multipartFile);
            uploadImageController.uploadImage(multipartFile,imgname,folderUpload,"user");
        }else {
            imgname="default_avatar.png";
        }

        //check email is exist
        User checkEmailExist = userService.findByEmail(email);
        User checkUsernameExist = userService.findByUsername(username);
        if (checkEmailExist != null ||checkUsernameExist != null ) {
            if(checkEmailExist != null){
                model.addAttribute("error", "Email đã tồn tại, vui lòng sử dụng email khác.");
            }
            else if(checkUsernameExist != null){
                model.addAttribute("error", "Username đã tồn tại, vui lòng sử dụng usename khác.");
            }
            model.addAttribute("emailAdd", email);
            model.addAttribute("us", username);
            model.addAttribute("ph", phone);
            model.addAttribute("add", address);
            model.addAttribute("full", fullname);
            return "admin/user/add-user";
        }
        else{
            User user = new User();
            String roleName = "";
            user.setUsername(username);
            user.setFullname(fullname);
            user.setEmail(email);
            user.setPhone(phone);
            user.setPassword(encoderPass.encode(password));
            user.setStatus(1);
            user.setCreateDate(new Date());
            user.setAddress(address);
            user.setGender(gender);
            user.setImage(imgname);

            try {
                if (birthday != null) {
                    Date dateBirthday = sdf.parse(birthday);
                    user.setBirthday(dateBirthday);
                } else {
                    user.setBirthday(null);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (role == 1) {
                roleName = "ADMIN";
            } else if (role == 2) {
                roleName = "USER";
            } else if (role == 3) {
                roleName = "MANAGER";
            }

            Role roleUser = roleRepository.findByRole(roleName);
            user.setRoles(Arrays.asList(roleUser));

            userService.createUser(user);

            redir.addFlashAttribute("success", "Thêm user thành công.");
            return "redirect:/admin/user/list-user";
        }
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
    public String updateUserPost(Model model,Principal principal,RedirectAttributes redir,HttpServletRequest request){
        getInfoUser(model,principal);

        Integer id= Integer.valueOf(request.getParameter("userId"));
        String username = request.getParameter("username");
        String fullname = request.getParameter("fullname");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        Integer gender = Integer.valueOf(request.getParameter("gender"));
        String birthday = request.getParameter("birthday");
        Integer role =Integer.valueOf(request.getParameter("role"));

        Optional<User> optionalUser= userService.findById(id);
        User user = optionalUser.get();

        user.setUsername(username);
        user.setFullname(fullname);
        user.setAddress(address);
        user.setPhone(phone);
        user.setGender(gender);
        user.setUpdateDate(new Date());
        user.setUpdateBy(principal.getName());
        String roleName="";
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
        }else if(role==2){
            roleName="USER";
        }else if(role==3){
            roleName="MANAGER";
        }

        Role roleUser = roleRepository.findByRole(roleName);
        List<Role> roles= new ArrayList<>(Arrays.asList(roleUser));
        user.setRoles(roles);
        userService.updateUser(user);
        redir.addFlashAttribute("success","Update user thành công.");
        String path="redirect:/admin/user/update/"+id;
        return path;
    }

    @GetMapping("/user/update-status")
    public String updateStatus(Model model,RedirectAttributes redirectAttributes, Principal principal,
                               @RequestParam("userId") Integer userId,
                               @RequestParam("status") Integer status){
        getInfoUser(model,principal);
        try {
            if(status==0){
                userService.updateStatus(userId,1);
                Optional<User> optionalUser= userService.findById(userId);
                User user= optionalUser.get();
                user.setUpdateBy(principal.getName());
                user.setUpdateDate(new Date());
                userService.updateUser(user);
            }else if(status==1){
                userService.updateStatus(userId,0);
            }
            redirectAttributes.addFlashAttribute("success","Update trạng thái thành công");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","Update trạng thái thất bại");
        }
        return "redirect:/admin/user/list-user";
    }

    @GetMapping("/user/delete")
    public String deleteUser(Model model,RedirectAttributes redirectAttributes,
                             Principal principal,
                             @RequestParam(value = "id") Integer id,
                             @RequestParam(value = "status") Integer status){
        getInfoUser(model,principal);
        try {
            if(status==1){
                userService.disableUser(id);
                redirectAttributes.addFlashAttribute("success","User đã được disable thành công.");
            }else {
                Optional<User> optionalUser= userService.findById(id);
                userService.deleteUser(optionalUser.get());
                redirectAttributes.addFlashAttribute("success","Xóa user thành công.");
            }
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","Xóa user thất bại");
        }
        return "redirect:/admin/user/list-user";
    }

    @PostMapping("/user/update-image-user")
    public String updateImageNews(Model model, Principal principal,RedirectAttributes redr,
                                  @RequestParam("image") MultipartFile multipartFile,
                                  HttpServletRequest request){

        getInfoUser(model,principal);
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
        String path="redirect:/admin/user/update/"+userId;
        return path;
    }

    @PostMapping("/user/resetpassword")
    public String resetPassword(Model model,Principal principal,@RequestParam("userId") Integer userId,RedirectAttributes red){
        getInfoUser(model,principal);
        User user= userService.findById(userId).get();
        try {
            String newPass="123";
            user.setPassword(encoderPass.encode(newPass));
            userService.updateUser(user);
            red.addFlashAttribute("success","Reset password thành công");
        }
        catch (Exception e){
            red.addFlashAttribute("error","Reset password thất bại");
        }
        String path="redirect:/admin/user/update/"+userId;
        return path;


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
