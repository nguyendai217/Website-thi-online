package com.wru.onthi.controller.admin;

import com.google.common.base.Strings;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.wru.onthi.entity.Subject;
import com.wru.onthi.entity.User;
import com.wru.onthi.services.SubjectService;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Optional;

@Controller
@RequestMapping("/subject")
public class SubjectManagerController {

    @Autowired
    UserService userService;

    @Autowired
    SubjectService subjectService;

    @Value("${folder.upload}")
    private String folderUpload;

    @GetMapping("/list-subject")
    public String listSubject(Model model, Principal principal,Pageable pageable,String subject){
        getInfoUser(model,principal);

        // pageable list subject
        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<Subject> pageSubject= null;
        if(subject== null || subject==""){
            pageSubject = subjectService.getAllSubject(pageItem);
        }else {
            pageSubject= subjectService.searchSubject(subject,pageItem);
        }

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
        UploadImageController uploadImageController= new UploadImageController();
        String imgname= uploadImageController.getImageName(multipartFile);
        if(!Strings.isNullOrEmpty(code) && !Strings.isNullOrEmpty(name)){
            Subject checkCodeExist = subjectService.findBySubjectCode(code);
            if(checkCodeExist != null){
                model.addAttribute("error","Mã môn học đã tồn tại");
            }else {
                Subject subject= new Subject();
                subject.setCode(code);
                subject.setName(name);
                subject.setImage(imgname);
                subject.setStatus(1);
                try {
                    subjectService.createSubject(subject);
                    uploadImageController.uploadImage(multipartFile,imgname,folderUpload,"subject");
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
        UploadImageController uploadImageController= new UploadImageController();
        String imgname= uploadImageController.getImageName(multipartFile);
        Optional<Subject> subject= subjectService.findBySubjectId(id);
        Subject sb= subject.get();
        if(!Strings.isNullOrEmpty(code) && !Strings.isNullOrEmpty(name)){
            sb.setCode(code);
            sb.setName(name);
            sb.setImage(imgname);
            try {
                subjectService.updateSubject(sb);
                uploadImageController.uploadImage(multipartFile,imgname,folderUpload,"subject");
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

//    @GetMapping("/search-subject")
//    public String searchSubject(Model model, Principal principal,Pageable pageable,@RequestParam("subject") String subject){
//        getInfoUser(model,principal);
//
//        int pageNumber = pageable.getPageNumber();
//        int pageSize= 5;
//        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
//        Pageable newPageAble = PageRequest.of(pageNumber, pageSize);
//        Page<Subject> pageSubject = subjectService.searchSubject(subject,newPageAble);
//
//        int totalItem = (int) pageSubject.getTotalElements();
//        int itemPerPage= pageSize * (pageNumber +1);
//        if(itemPerPage > totalItem){
//            itemPerPage= totalItem;
//        }
//        model.addAttribute("pageInfo",pageSubject);
//        model.addAttribute("total",totalItem);
//        model.addAttribute("itemPerPage",itemPerPage);
//        model.addAttribute("sbj",subject);
//        return "admin/subject/list-subject";
//    }

    @GetMapping("/delete/{id}/{status}")
    public String deleteSubject(Model model, Principal principal,RedirectAttributes redr,
                                @PathVariable("id") Integer id,
                                @PathVariable("status") Integer status) {
        getInfoUser(model,principal);
        try {
            if(status==1) {
                subjectService.disableSubject(id);
                redr.addFlashAttribute("success", "Môn học đã được disable.");
            }else if (status==0){
                Optional<Subject> optionalSubject= subjectService.findBySubjectId(id);
                Subject subject= optionalSubject.get();
                subjectService.deleteSubject(subject);
                redr.addFlashAttribute("success", "Xóa môn học thành công.");
            }
        }catch (Exception e){
            redr.addFlashAttribute("error","Xóa môn học thất bại");
        }
        return "redirect:/subject/list-subject";
    }

    @GetMapping("/update-status")
    public String updateStatus(Model model, Principal principal,RedirectAttributes redr,
                               @RequestParam("id") Integer id,
                               @RequestParam("status") Integer status){
        getInfoUser(model,principal);
        try {
            if(status==1){
                subjectService.updateStatus(id,0);
            }else if(status==0){
                subjectService.updateStatus(id,1);
            }
            redr.addFlashAttribute("success","Update trạng thái thành công");
        }catch (Exception e){
            redr.addFlashAttribute("error","Update trạng thái thất bại");
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


}
