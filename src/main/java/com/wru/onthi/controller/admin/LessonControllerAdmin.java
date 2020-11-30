package com.wru.onthi.controller.admin;

import com.google.common.base.Strings;
import com.wru.onthi.entity.Classroom;
import com.wru.onthi.entity.Lesson;
import com.wru.onthi.entity.Subject;
import com.wru.onthi.entity.User;
import com.wru.onthi.model.LessonModel;
import com.wru.onthi.services.ClassroomService;
import com.wru.onthi.services.LessonService;
import com.wru.onthi.services.SubjectService;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/list-lesson")
    public String getAllLesson(Model model, Principal principal,String lessonName,
                               String subjectId, String classId,Pageable pageable){
        getInfoUser(model,principal);

        //get AllClassroom
        List<Classroom> listClass= classroomService.getAllClassroom();
        model.addAttribute("listClass",listClass);

        //get list Subject
        List<Subject> listSubject= subjectService.getlistSubject();
        model.addAttribute("listSubject", listSubject);

        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize);
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
            model.addAttribute("pageInfo", pageLesson);
            model.addAttribute("total",totalItem);
            model.addAttribute("itemPerPage",itemPerPage);
            model.addAttribute("path","/lesson/list-lesson");
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
    public @ResponseBody String addLessonPost(Model model, Principal principal,RedirectAttributes redir,
                                @RequestBody LessonModel lessonModel){

        getInfoUser(model,principal);

        String lessonName= lessonModel.getLessonName();
        String content= lessonModel.getLessonContent();
        String classId= lessonModel.getClassroom();
        String subjectId= lessonModel.getSubject();

        Optional<Subject> optional= subjectService.findBySubjectId(Integer.valueOf(subjectId));
        Optional<Classroom> optionalClass= classroomService.findById(Integer.valueOf(classId));
        Lesson lesson= new Lesson();
        lesson.setCreateDate(new Date());
        lesson.setLessonContent(content);
        lesson.setLessonName(lessonName);
        lesson.setSubject(optional.get());
        lesson.setViews(0);
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

    @GetMapping("/delete-lesson/{id}")
    public String deleteLesson(Model model, Principal principal, RedirectAttributes redr,
                               @PathVariable("id") Integer id){
        getInfoUser(model,principal);
        try {
            lessonService.deleteLesson(id);
            redr.addFlashAttribute("success","Xóa bài học thành công");
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
