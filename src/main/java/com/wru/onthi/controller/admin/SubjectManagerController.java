package com.wru.onthi.controller.admin;

import com.google.common.base.Strings;
import com.wru.onthi.entity.Subject;
import com.wru.onthi.entity.User;
import com.wru.onthi.services.SubjectService;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;

@Controller
@RequestMapping("/subject")
public class SubjectManagerController {

    @Autowired
    UserService userService;

    @Autowired
    SubjectService subjectService;

    @GetMapping("/list-subject")
    public String listSubject(Model model, Principal principal,Pageable pageable){
        getInfoUser(model,principal);

        // pageable list subject
        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize);
        Page<Subject> pageSubject = subjectService.getAllSubject(pageItem);

        int totalItem = (int) pageSubject.getTotalElements();
        int itemPerPage= pageSize * (pageNumber+1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }
        model.addAttribute("pageInfo",pageSubject);
        model.addAttribute("total",totalItem);
        model.addAttribute("itemPerPage",itemPerPage);
        model.addAttribute("pageInfo",pageSubject);
        model.addAttribute("path","/subject/list-subject");
        return "admin/subject/list-subject";
    }

    @GetMapping("/add-subject")
    public String addSubjectGet(Model model,Principal principal){
        getInfoUser(model,principal);
        return "admin/subject/add-subject";
    }

    @PostMapping("/add-subject")
    public String addSubjectPost(Model model, Principal principal, RedirectAttributes redir,
                                 @RequestParam("codeSubject") String code,
                                 @RequestParam("nameSubject") String name,
                                 @RequestParam("image") MultipartFile multipartFile) throws IOException {
        getInfoUser(model,principal);
        if(!Strings.isNullOrEmpty(code) && !Strings.isNullOrEmpty(name)){
            Subject checkCodeExist = subjectService.findBySubjectCode(code);
            if(checkCodeExist != null){
                model.addAttribute("error","Mã môn học đã tồn tại");
            }else {
                String fileImage= StringUtils.cleanPath(multipartFile.getOriginalFilename());
                Subject subject= new Subject();
                subject.setCode(code);
                subject.setName(name);
                subject.setImage(fileImage);
                subject.setStatus(1);
                try {
                    subjectService.createSubject(subject);
                    UploadImage(multipartFile,fileImage);
                    redir.addFlashAttribute("success","Thêm mới môn học thành công");
                }catch (Exception e){
                    model.addAttribute("error","Thêm mới môn học thất bại");
                }

            }
        }
        return "redirect:/subject/list-subject";
    }

    @GetMapping("/update/{id}")
    public String updateGet(Model model, Principal principal, @PathVariable("id") Integer id){
        getInfoUser(model,principal);
        Optional<Subject> optional= subjectService.findBySubjectId(id);
        Subject subject= optional.get();
        model.addAttribute("subject",subject);
        return "admin/subject/update-subject";
    }

    @PostMapping("/update-subject")
    public String updateSubjectPost(Model model, Principal principal,RedirectAttributes redir,
                                    @RequestParam(value = "id") Integer id,
                                    @RequestParam(value = "codeSubject") String code,
                                    @RequestParam(value = "nameSubject") String name,
                                    @RequestParam("image") MultipartFile multipartFile){
        getInfoUser(model,principal);

        Optional<Subject> subject= subjectService.findBySubjectId(id);
        Subject sb= subject.get();
        if(!Strings.isNullOrEmpty(code) && !Strings.isNullOrEmpty(name)){
            String fileImage= StringUtils.cleanPath(multipartFile.getOriginalFilename());
            sb.setCode(code);
            sb.setName(name);
            sb.setImage(fileImage);
            try {
                subjectService.updateSubject(sb);
                UploadImage(multipartFile,fileImage);
                redir.addFlashAttribute("success","Cập nhật môn học thành công");
                return "redirect:/subject/list-subject";
            }catch (Exception e){
                model.addAttribute("error","Cập nhật môn học thất bại");
                return "admin/subject/update-subject";
            }
        }else {
            return "admin/subject/update-subject";
        }
    }

    @GetMapping("/search-subject")
    public String searchSubject(Model model, Principal principal,Pageable pageable,@RequestParam("subject") String subject){
        getInfoUser(model,principal);

        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable newPageAble = PageRequest.of(pageNumber, pageSize);
        Page<Subject> pageSubject = subjectService.searchSubject(subject,newPageAble);

        int totalItem = (int) pageSubject.getTotalElements();
        int itemPerPage= pageSize * (pageNumber +1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }
        model.addAttribute("pageInfo",pageSubject);
        model.addAttribute("total",totalItem);
        model.addAttribute("itemPerPage",itemPerPage);
        model.addAttribute("sbj",subject);
        return "admin/subject/list-subject";
    }

    @GetMapping("/delete/{id}")
    public String deleteSubject(Model model, Principal principal,RedirectAttributes redr,
                                @PathVariable("id") Integer id) {
        getInfoUser(model,principal);
        Optional<Subject> optional= subjectService.findBySubjectId(id);
        Subject subject= optional.get();
        try {
            subjectService.deleteSubject(subject);
            redr.addFlashAttribute("success","Xóa môn học thành công");
        }catch (Exception e){
            redr.addFlashAttribute("error","Xóa môn học thất bại");
        }
        return "redirect:/subject/list-subject";
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
        String uploadDir="src/main/resources/static/image/subject/";
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
