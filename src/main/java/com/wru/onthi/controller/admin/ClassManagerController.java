package com.wru.onthi.controller.admin;

import com.google.common.base.Strings;
import com.wru.onthi.entity.Classroom;
import com.wru.onthi.entity.School;
import com.wru.onthi.entity.Subject;
import com.wru.onthi.entity.User;
import com.wru.onthi.services.ClassroomService;
import com.wru.onthi.services.SchoolService;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ClassManagerController {

    @Autowired
    UserService userService;

    @Autowired
    ClassroomService classroomService;

    @Autowired
    SchoolService schoolService;

    @GetMapping("/class/list-class")
    public String listClass(Model model, Principal principal, Pageable pageable){
       getInfoUser(model,principal);

        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        Page<Classroom> pageClass = classroomService.getAllClass(pageItem);

        int totalItem = (int) pageClass.getTotalElements();
        int itemPerPage= pageSize * (pageNumber+1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }
        model.addAttribute("pageInfo",pageClass);
        model.addAttribute("total",totalItem);
        model.addAttribute("itemPerPage",itemPerPage);
       return "admin/classroom/list-class";
    }

    @GetMapping("/class/add-class")
    public String addClass(Model model, Principal principal){
        getInfoUser(model,principal);

        List<School> listSchool= new ArrayList<>();
        listSchool= schoolService.getListSchool();
        model.addAttribute("listSchool",listSchool);
        return "admin/classroom/add-class";
    }

    @PostMapping("class/add-class")
    public String addSubjectPost(Model model, Principal principal, RedirectAttributes redir,
                                 @RequestParam("classCode") String classcode,
                                 @RequestParam("className") String classname,
                                 @RequestParam("status") Integer status,
                                 @RequestParam("image") MultipartFile multipartFile) throws IOException {
        getInfoUser(model,principal);
        if(!Strings.isNullOrEmpty(classcode) && !Strings.isNullOrEmpty(classname)){
            Classroom checkClassExist = classroomService.findByClassCode(classcode);
            if(checkClassExist != null){
                model.addAttribute("error","Lớp học đã tồn tại");
            }else {
                String fileImage= StringUtils.cleanPath(multipartFile.getOriginalFilename());
                Classroom classroom= new Classroom();
                classroom.setCode(classcode);
                classroom.setClassname(classname);
                classroom.setImage(fileImage);
                classroom.setStatus(status);
                try {
                    classroomService.createClass(classroom);
                    UploadImage(multipartFile,fileImage);
                    redir.addFlashAttribute("success","Thêm mới lớp học thành công");
                }catch (Exception e){
                    model.addAttribute("error","Thêm mới lớp học thất bại");
                }
            }
        }
        return "redirect:/class/list-class";
    }

    @GetMapping("class/update/{id}")
    public String updateGet(Model model, Principal principal, @PathVariable("id") Integer id){
        getInfoUser(model,principal);
        Optional<Classroom> optional= classroomService.findById(id);
        Classroom classroom = optional.get();
        model.addAttribute("cl",classroom);
        return "admin/classroom/update-class";
    }
//
    @PostMapping("class/update-class")
    public String updateClassroomPost(Model model, Principal principal, RedirectAttributes redir,
                                    @RequestParam(value = "idClass") Integer id,
                                    @RequestParam(value = "classCode") String code,
                                    @RequestParam(value = "className") String name,
                                    @RequestParam(value = "status") Integer status,
                                    @RequestParam("image") MultipartFile multipartFile){
        getInfoUser(model,principal);

        Optional<Classroom> optional= classroomService.findById(id);
        Classroom classroom= optional.get();
        String fileImage= null;
        if(!Strings.isNullOrEmpty(code) && !Strings.isNullOrEmpty(name)){
            if(!multipartFile.isEmpty()){
                fileImage= StringUtils.cleanPath(multipartFile.getOriginalFilename());
                classroom.setImage(fileImage);
            }
            classroom.setCode(code);
            classroom.setClassname(name);
            classroom.setStatus(status);
            try {
                classroomService.updateClass(classroom);
                if(!multipartFile.isEmpty()){
                    UploadImage(multipartFile,fileImage);
                }
                redir.addFlashAttribute("success","Cập nhật lớp học thành công");
                return "redirect:/class/list-class";
            }catch (Exception e){
                model.addAttribute("error","Cập nhật lớp học thất bại");
                model.addAttribute("cl",classroom);
                return "admin/classroom/update-class";
            }
        }else {
            return "admin/classroom/update-class";
        }
    }

    @GetMapping("class/delete/{id}")
    public String deleteClass(Model model, Principal principal,RedirectAttributes redr,
                                @PathVariable("id") Integer id) {
        getInfoUser(model,principal);
        Optional<Classroom> optional=classroomService.findById(id);
        Classroom classroom= optional.get();
        try {
            classroomService.deleteClass(classroom);
            redr.addFlashAttribute("success","Xóa lớp học thành công");
        }catch (Exception e){
            redr.addFlashAttribute("error","Xóa lớp học thất bại");
        }
        return "redirect:/class/list-class";
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

    private void UploadImage(MultipartFile multipartFile,String fileImage) throws IOException {
        String uploadDir="src/main/resources/static/image/classroom/";
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
}
