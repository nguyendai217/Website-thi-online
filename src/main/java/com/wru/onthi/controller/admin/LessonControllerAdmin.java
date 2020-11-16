package com.wru.onthi.controller.admin;

import com.wru.onthi.entity.Classroom;
import com.wru.onthi.entity.Lesson;
import com.wru.onthi.entity.Subject;
import com.wru.onthi.entity.User;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
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
    public String getAllLesson(Model model, Principal principal,Pageable pageable){
        getInfoUser(model,principal);


        //get AllClassroom
        List<Classroom> lístClass= classroomService.getAllClassroom();
        model.addAttribute("listClass",lístClass);

        //get list Subject
        List<Subject> listSubject= subjectService.getlistSubject();
        model.addAttribute("listSubject", listSubject);



        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize);
        Page<Lesson> pageLesson = lessonService.getAllLesson(pageItem);

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
            return "admin/lesson/list-lesson";
        }
    }

    @GetMapping("/add-lesson-manager")
    public String addLessonGet(Model model,Principal principal){
        getInfoUser(model,principal);
        return "admin/lesson/add-lesson";
    }

    @GetMapping("/update-lesson/{id}")
    public String updateLessonGet(Model model, Principal principal, @PathVariable("id") Integer id){
        getInfoUser(model,principal);
        Optional<Lesson> optional= lessonService.findByLessonId(id);
        Lesson lesson= optional.get();
        model.addAttribute("lesson",lesson);
        return "admin/lesson/update-lesson";
    }

    @GetMapping("/delete-lesson/{id}")
    public String deleteLesson(Model model, Principal principal, RedirectAttributes redr,
                               @PathVariable("id") Integer id){
        getInfoUser(model,principal);
        Optional<Lesson> optional= lessonService.findByLessonId(id);
        Lesson lesson= optional.get();
        try {
            lessonService.deleteLesson(lesson);
            redr.addFlashAttribute("success","Xóa bài học thành công");
        }catch (Exception e){
            redr.addFlashAttribute("error","Xóa bài học thất bại");
        }
        return "redirect:/lesson/list-lesson-manager";
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
