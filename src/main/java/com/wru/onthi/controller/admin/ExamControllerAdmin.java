package com.wru.onthi.controller.admin;

import com.wru.onthi.entity.*;
import com.wru.onthi.repository.ClassRoomRepository;
import com.wru.onthi.services.ClassroomService;
import com.wru.onthi.services.ExamService;
import com.wru.onthi.services.SubjectService;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
@RequestMapping("/exam")
public class ExamControllerAdmin {
    @Autowired
    UserService userService;

    @Autowired
    ExamService examService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    ClassroomService classroomService;

    @GetMapping("/list-exam")
    public String getListExam(Model model, Principal principal,Pageable pageable,String codeExam ,String subjectId,String classId){
        getInfoUser(model,principal);
//        String codeExam = request.getParameter("codeExam");
//        String subjectId= request.getParameter("subjectId");
//        String classId= request.getParameter("classId");
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

        Page<Exam> pageExam = null;
        if((codeExam== null || codeExam =="") && (subjectId== null || subjectId =="")
                && (classId==null|| classId=="")){
            pageExam=examService.getAllListExam(pageItem);
        }
        else {
            pageExam=examService.searchExam(codeExam,subjectId,classId,pageItem);
        }
        int totalItem = (int) pageExam.getTotalElements();
        int itemPerPage= pageSize * (pageNumber+1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }

        if(pageExam == null){
            model.addAttribute("error","Danh sách bài thi trống.");
            return "admin/exam/list-exam";
        } else {
            model.addAttribute("pageInfo", pageExam);
            model.addAttribute("total",totalItem);
            model.addAttribute("itemPerPage",itemPerPage);
            model.addAttribute("path","/exam/list-exam");
            return "admin/exam/list-exam";
        }
    }

    @GetMapping("/update-exam/{id}")
    public String updateExamGet(Model model, Principal principal,@PathVariable("id") Integer id){
        getInfoUser(model,principal);

        //get AllClassroom
        List<Classroom> lístClass= classroomService.getAllClassroom();
        model.addAttribute("listClass",lístClass);

        //get list Subject
        List<Subject> listSubject= subjectService.getlistSubject();
        model.addAttribute("listSubject", listSubject);

        Optional<Exam> optionalExam= examService.findByExamId(id);
        Exam exam = optionalExam.get();
        model.addAttribute("exam",exam);
        return "admin/exam/update-exam";
    }

    @PostMapping("/update-exam")
    public String updateExamPost(Model model, Principal principal, HttpServletRequest request, RedirectAttributes redr){
        getInfoUser(model,principal);
        Integer id=Integer.valueOf(request.getParameter("examId"));
        String examCode= request.getParameter("examCode");
        String title= request.getParameter("title");
        Integer classId= Integer.valueOf(request.getParameter("classId"));
        Integer subjectId=Integer.valueOf(request.getParameter("subjectId"));
        Integer totalQuestion= Integer.valueOf(request.getParameter("totalQuestion"));
        Integer timeOut= Integer.valueOf(request.getParameter("timeOut"));
        String content= request.getParameter("content");

        Optional<Subject> optionalSubject= subjectService.findBySubjectId(subjectId);
        Subject subject= optionalSubject.get();

        Optional<Classroom> optionalClassroom= classroomService.findById(classId);
        Classroom classroom= optionalClassroom.get();
        try {
            Optional<Exam> optionalExam= examService.findByExamId(id);
            Exam exam= optionalExam.get();
            exam.setCodeExam(examCode);
            exam.setTitle(title);
            exam.setExam_subject(subject);
            exam.setExam_classroom(classroom);
            exam.setTitle(title);
            exam.setTotalQuestion(totalQuestion);
            exam.setContent(content);
            exam.setTimeOut(timeOut);
            examService.updateExam(exam);
            redr.addFlashAttribute("success","Update thành công");
            return "redirect:/exam/list-exam";
        }catch (Exception e){
            model.addAttribute("error","Update thông tin đề thi thất bại");
            String path= "redirect:/exam/update-exam/"+ id;
            return path;
        }
    }

    @GetMapping("/add-exam")
    public String addExamGet(Model model, Principal principal){
        getInfoUser(model,principal);
        //get AllClassroom
        List<Classroom> lístClass= classroomService.getAllClassroom();
        model.addAttribute("listClass",lístClass);

        //get list Subject
        List<Subject> listSubject= subjectService.getlistSubject();
        model.addAttribute("listSubject", listSubject);
        return "admin/exam/add-exam";
    }

    @PostMapping("/add-exam")
    public String addExamPost(Model model,Principal principal,HttpServletRequest request,RedirectAttributes redr){
        getInfoUser(model,principal);
        String title= request.getParameter("title");
        Integer subjectId= Integer.valueOf(request.getParameter("subjectId"));
        Integer classId= Integer.valueOf(request.getParameter("classId"));
        Integer totalQuestion= Integer.valueOf(request.getParameter("totalQuestion"));
        Integer timeOut= Integer.valueOf(request.getParameter("timeOut"));
        String content= request.getParameter("content");

        Optional<Subject> optional= subjectService.findBySubjectId(subjectId);
        Subject subject= optional.get();
        String subjectCode= subject.getCode();
        String codeExam= randomCodeExam(subjectCode);

        Optional<Classroom> optionalClassroom= classroomService.findById(classId);
        Classroom classroom= optionalClassroom.get();

        try {
            Exam exam= new Exam();
            exam.setTitle(title);
            exam.setExam_subject(subject);
            exam.setExam_classroom(classroom);
            exam.setTotalQuestion(totalQuestion);
            exam.setTimeOut(timeOut);
            exam.setContent(content);
            exam.setCreateBy(principal.getName());
            exam.setViews(0);
            exam.setCreateDate(new Date());
            exam.setCodeExam(codeExam);
            examService.createExam(exam);
            redr.addFlashAttribute("success","Thêm mới đề thi thành công, mã đề thi:"+ codeExam);
            return "redirect:/exam/list-exam";
        }catch (Exception e){
            e.printStackTrace();
            redr.addFlashAttribute("error","Thêm mới đề thi thất bại.");
            return "admin/exam/add-exam";
        }
    }

    public String randomCodeExam(String subjectCode){
        Random random= new Random();
        Integer number= random.nextInt(99999);
        String codeExam= subjectCode+ number;
        return codeExam;
    }

    @GetMapping("/delete-exam/{examId}")
    public String deleteExam(Model model, Principal principal,@PathVariable("examId") Integer examId,RedirectAttributes redr){
        getInfoUser(model,principal);
        try {
            examService.deleteExam(examId);
            redr.addFlashAttribute("success","Xóa đề thi thành công");
            return "redirect:/exam/list-exam";
        }catch (Exception e){
            e.printStackTrace();
            redr.addFlashAttribute("error","Xóa đề thi thất bại");
            return "redirect:/exam/list-exam";
        }
    }

    @GetMapping("/update-status")
    public String updateStatus(Model model,Principal principal,
                               @RequestParam("examId") Integer examId,
                               @RequestParam("status") Integer status,
                               RedirectAttributes redir){
        getInfoUser(model,principal);
        try {
            if(status==1){
                examService.updateStatus(examId,0);
            }else if(status==0){
                examService.updateStatus(examId,1);
            }
            redir.addFlashAttribute("success","Update trạng thái thành công.");
        }catch (Exception e){
            redir.addFlashAttribute("error","Update trạng thái thất bại");
        }
        return "redirect:/exam/list-exam";
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
