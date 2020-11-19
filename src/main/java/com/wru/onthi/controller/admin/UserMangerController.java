package com.wru.onthi.controller.admin;

import com.sun.java.swing.plaf.windows.WindowsTextAreaUI;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/admin")
public class UserMangerController {
    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder encoderPass;

    @Autowired
    RoleRepository roleRepository;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    @GetMapping("/user/list-user")
    public String getAllUser(Model model, Principal principal, Pageable pageable,
                             String username,String email, String phone){
        // pageable list user
        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable newPageAble = PageRequest.of(pageNumber, pageSize);
        Page<User> pageUser= null;
        if(username== null && email == null && phone== null){
            pageUser = userService.getAllUser(newPageAble);
        }else {
            pageUser= userService.searchUser(username,email,phone,pageable);
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
    public String addUserGet(Model model, Principal principal, RedirectAttributes redir,HttpServletRequest request) {
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
            //user.setBirthday(birthday);
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
        return "redirect:/admin/user/list-user";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(Model model,RedirectAttributes redirectAttributes, Principal principal,@PathVariable(value = "id") Integer id){
        getInfoUser(model,principal);
        Optional<User> optionalUser= userService.findById(id);
        User user= optionalUser.get();
        try {
            userService.deleteUser(user);
            redirectAttributes.addFlashAttribute("success","Xóa user thành công.");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","Delete user thất bại");
        }
        return "redirect:/admin/user/list-user";
    }

    @PostMapping("/user/updateImage")
    public String updateImage(@RequestParam("fileImage") MultipartFile multipartFile,
                              @RequestParam("userId") Integer id) {
        Date date= new Date();
        long time= date.getTime();
        String strName= String.valueOf(time);

        String flName = multipartFile.getOriginalFilename();
        String[] flPath = flName.split("[.]");
        String flExtension = flPath[flPath.length - 1];
        String imgname= strName +"."+flExtension;

       // String fileImage= StringUtils.cleanPath(multipartFile.getOriginalFilename());
        try {
            Optional<User> optional= userService.findById(id);
            User user= optional.get();
            user.setImage(imgname);
            userService.updateUser(user);
            UploadImage(multipartFile,imgname);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path= "redirect:/admin/user/update/"+id;
        return path;
    }

    private void UploadImage(MultipartFile multipartFile, String fileImage) throws IOException {
        String uploadDir="src/main/resources/static/image/user/";
        Path uploadpath= Paths.get(uploadDir);
        if(! Files.exists(uploadpath)){
            Files.createDirectories(uploadpath);
        }

        try(InputStream inputStream= multipartFile.getInputStream()) {
            Path filePath= uploadpath.resolve(fileImage);
            System.out.print(filePath.toString());
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new IOException("Could not upload file"+ fileImage);
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
