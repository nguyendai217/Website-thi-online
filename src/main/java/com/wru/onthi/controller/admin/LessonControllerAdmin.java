package com.wru.onthi.controller.admin;

import com.google.common.base.Strings;
import com.wru.onthi.entity.*;
import com.wru.onthi.model.LessonModel;
import com.wru.onthi.services.ClassroomService;
import com.wru.onthi.services.LessonService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/lesson")
public class LessonControllerAdmin {

    @Autowired
    UserService userService;

    @Autowired
    LessonService lessonService;
    @Autowired
    ClassroomService classroomService;

    @Autowired
    SubjectService subjectService;

    @Value("${folder.upload}")
    private String foldeUpload;

    @GetMapping("/list-lesson")
    public String getAllLesson(Model model, Principal principal,String lessonName,
                               String subjectId, String classId,Pageable pageable){
        getInfoUser(model,principal);

        if (subjectId == "") subjectId = null;
        if (lessonName == "") lessonName = null;
        if (classId == "") classId = null;

        //get AllClassroom
        List<Classroom> listClass= classroomService.getAllClassroom();
        model.addAttribute("listClass",listClass);

        //get list Subject
        List<Subject> listSubject= subjectService.getlistSubject();
        model.addAttribute("listSubject", listSubject);

        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<Lesson> pageLesson= null;
        if((lessonName == "" || lessonName==null) &&
                (subjectId == "" || subjectId == null) && (classId == "" ||classId == null)){
            pageLesson = lessonService.getAllLesson(pageItem);
        }else {
            pageLesson = lessonService.searchLesson(lessonName,subjectId,classId,pageItem);
        }

        int totalItem = (int) pageLesson.getTotalElements();
        int itemPerPage= pageSize * (pageNumber+1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }

        if(pageLesson == null){
            model.addAttribute("error","Danh sách bài học trống.");
            return "admin/lesson/list-lesson";
        } else {

            if(lessonName == null){
                lessonName="";
            }
            if(subjectId== null){
                subjectId="";
            }
            if(classId== null){
                classId="";
            }
            model.addAttribute("pageInfo", pageLesson);
            model.addAttribute("total",totalItem);
            model.addAttribute("itemPerPage",itemPerPage);
            model.addAttribute("path","/lesson/list-lesson");
            model.addAttribute("condition", ("lessonName="+lessonName+"&subjectId="+subjectId+"&classId="+classId));
            return "admin/lesson/list-lesson";
        }
    }
    @GetMapping("/add-lesson")
    public String addLessonGet(Model model,Principal principal){
        getInfoUser(model,principal);

        //get AllClassroom
        List<Classroom> listClass= classroomService.getAllClassroom();
        model.addAttribute("listClass",listClass);

        //get list Subject
        List<Subject> listSubject= subjectService.getlistSubject();
        model.addAttribute("listSubject", listSubject);

        return "admin/lesson/add-lesson";
    }

    @RequestMapping(value = "/add-lesson",method = RequestMethod.POST)
    public String addLessonPost(Model model,Principal principal,
                                HttpServletRequest request,
                                RedirectAttributes redir,
                                @RequestParam("image") MultipartFile multipartFile) throws IOException {

        getInfoUser(model,principal);

        String lessonName = request.getParameter("lessonName");
        String description= request.getParameter("description");
        Integer classId= Integer.valueOf(request.getParameter("classId"));
        Integer subjectId=Integer.valueOf(request.getParameter("subjectId"));
        String content= request.getParameter("content");
        String imgname= null;
        if(multipartFile != null){
            UploadImageController uploadImageController= new UploadImageController();
            imgname= uploadImageController.getImageName(multipartFile);
            uploadImageController.uploadImage(multipartFile,imgname,foldeUpload,"lesson");
        }
        Optional<Subject> optional= subjectService.findBySubjectId(subjectId);
        Optional<Classroom> optionalClass= classroomService.findById(classId);
        Lesson lesson= new Lesson();
        lesson.setCreateDate(new Date());
        lesson.setLessonContent(content);
        lesson.setLessonName(lessonName);
        lesson.setSubject(optional.get());
        lesson.setViews(0);
        lesson.setStatus(1);
        lesson.setImage(imgname);
        lesson.setDescription(description);
        lesson.setCreateBy(principal.getName());
        lesson.setClassroom(optionalClass.get());
        try {
            lessonService.createLesson(lesson);
            redir.addFlashAttribute("success","Thêm bài học thành công.");
            return "redirect:/lesson/list-lesson";
        }catch (Exception e){
            redir.addFlashAttribute("error","Thêm bài học thất bại.");
            return "admin/lesson/add-lesson";
        }
    }

    @PostMapping("/update-lesson")
    public String updateLessonPost(Model model, Principal principal,HttpServletRequest request, RedirectAttributes redr){
        getInfoUser(model,principal);
        Integer lessonId=Integer.valueOf(request.getParameter("lessonId"));
        String lessonname = request.getParameter("lessonname");
        Integer classId= Integer.valueOf(request.getParameter("classId"));
        Integer subjectId=Integer.valueOf(request.getParameter("subjectId"));
        String lessonContent= request.getParameter("content");

        Optional<Lesson> optionalLesson= lessonService.findByLessonId(lessonId);
        Lesson lesson= optionalLesson.get();
        Optional<Subject> optionalSubject= subjectService.findBySubjectId(subjectId);
        Optional<Classroom> optionalClass= classroomService.findById(classId);
        try{
            lesson.setLessonName(lessonname);
            lesson.setLessonContent(lessonContent);
            lesson.setSubject(optionalSubject.get());
            lesson.setClassroom(optionalClass.get());
            lesson.setUpdateDate(new Date());
            lesson.setUpdateBy(principal.getName());
            lessonService.updateLesson(lesson);
            redr.addFlashAttribute("success","Update bài học thành công");
            return "redirect:/lesson/list-lesson";
        }catch (Exception e){
            redr.addFlashAttribute("error","Update bài học thất bại");
            String path= "/lesson/update-lesson/"+lessonId;
            return "redirect:"+path;
        }
    }

    @GetMapping("/update-lesson/{id}")
    public String updateLessonGet(Model model, Principal principal, @PathVariable("id") Integer id){
        getInfoUser(model,principal);

        //get AllClassroom
        List<Classroom> listClass= classroomService.getAllClassroom();
        model.addAttribute("listClass",listClass);

        //get list Subject
        List<Subject> listSubject= subjectService.getlistSubject();
        model.addAttribute("listSubject", listSubject);

        Optional<Lesson> optional= lessonService.findByLessonId(id);
        Lesson lesson= optional.get();
        model.addAttribute("lesson",lesson);
        return "admin/lesson/update-lesson";
    }

    @PostMapping("/update-image-lesson")
    public String updateImageLesson(Model model, Principal principal,
                                    RedirectAttributes redr,
                                    HttpServletRequest request,
                                    @RequestParam("image") MultipartFile multipartFile){
        getInfoUser(model,principal);
        UploadImageController uploadImageController= new UploadImageController();
        String imgname= uploadImageController.getImageName(multipartFile);

        Integer lessonId= Integer.valueOf(request.getParameter("lessonId"));
        Optional<Lesson> optionalLesson= lessonService.findByLessonId(lessonId);
        Lesson lesson= optionalLesson.get();

        try {
            lesson.setImage(imgname);
            lessonService.updateLesson(lesson);
            uploadImageController.uploadImage(multipartFile,imgname,foldeUpload,"lesson");
            redr.addFlashAttribute("success","Update hình ảnh bài học thành công");
            return "redirect:/lesson/list-lesson";
        }catch (Exception e){
            e.printStackTrace();
            redr.addFlashAttribute("error","update hình ảnh bài học thất bại");
            return "redirect:/lesson/list-lesson";
        }

    }

    @GetMapping("/delete-lesson")
    public String deleteLesson(Model model, Principal principal,
                               RedirectAttributes redr,
                               @RequestParam("id") Integer id,
                               @RequestParam("status") Integer status){
        getInfoUser(model,principal);
        try {
            if(status==1){
                lessonService.disableLesson(id);
                redr.addFlashAttribute("success","Disable bài học thành công");
            }else {
                Optional<Lesson> optionalLesson= lessonService.findByLessonId(id);
                lessonService.deleteLesson(optionalLesson.get());
                redr.addFlashAttribute("success","Xóa bài học thành công");
            }
        }catch (Exception e){
            redr.addFlashAttribute("error","Xóa bài học thất bại");
        }
        return "redirect:/lesson/list-lesson";
    }

    @GetMapping("/update-status")
    public String updateStatus(Model model,Principal principal,
                               @RequestParam("id") Integer id,
                               @RequestParam("status") Integer status,
                               RedirectAttributes redir){
        getInfoUser(model,principal);
        try {
            if(status==1){
                lessonService.updateStatus(id,0);
                Optional<Lesson>optionalLesson= lessonService.findByLessonId(id);
                Lesson lesson=optionalLesson.get();
                lesson.setUpdateBy(principal.getName());
                lesson.setUpdateDate(new Date());
                lessonService.updateLesson(lesson);
            }else if(status==0){
                lessonService.updateStatus(id,1);
            }
            redir.addFlashAttribute("success","Update trạng thái thành công.");
        }catch (Exception e){
            redir.addFlashAttribute("error","Update trạng thái thất bại");
        }
        return "redirect:/lesson/list-lesson";
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
