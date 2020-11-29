package com.wru.onthi.controller;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.wru.onthi.entity.*;
import com.wru.onthi.model.LessonNew;
import com.wru.onthi.model.QuestionModel;
import com.wru.onthi.services.*;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ExamController {

    @Autowired
    ClassroomService classroomService;

    @Autowired
    ExamService examService;

    @Autowired
    LessonService lessonService;

    @Autowired
    QuestionService questionService;

    @Autowired
    NewsService newsService;


    @GetMapping("/kiemtra/list-class")
    public String getAllClass(Model model, Principal principal){
        getDefault(model,principal);
        //get ListClass
        List<Classroom> listClass= classroomService.getAllClassroom();
        model.addAttribute("listClass",listClass);
        return "exam/list-class";
    }

    @GetMapping("/lophoc/list-class")
    public String getClassBySubject(Model model, Principal principal, @RequestParam("subjectId") Integer subjectId){
        getDefault(model,principal);
        // get list class by subject

        List<Classroom> getListClassBySubject= classroomService.listClassBySubject(subjectId);
        model.addAttribute("subjectId",subjectId);
        model.addAttribute("listClass",getListClassBySubject);
        return "exam/list-class-bySubject";
    }

    //get list exam by subject

    @GetMapping("/kiemtra/list-exam")
    public String listExam(Model model,Principal principal,@RequestParam("subjectId") Integer subjectId,
                           @RequestParam("classId") Integer classId){
        getDefault(model,principal);
        List<Exam> listExam= examService.getListExamBySubjectAndClass(subjectId,classId);
        if(listExam.size()>0){
            model.addAttribute("listExams",listExam);
        }else {
            model.addAttribute("emptyExam","emptyExam");
        }
        return "exam/list-exam";
    }

    @GetMapping("/kiemtra/info")
    public String infoExam(Model model,Principal principal, @RequestParam("examId") Integer examId){
        getDefault(model,principal);
        Optional<Exam> optionalExam= examService.findByExamId(examId);
        Exam exam= optionalExam.get();
        model.addAttribute("exam",exam);
        model.addAttribute("examId",examId);
        return "exam/exam-info";
    }

    @GetMapping("/kiemtra")
    public String getListQuestion(Model model,Principal principal, @RequestParam("examId") Integer examId){
        getDefault(model,principal);
        Optional<Exam> optionalExam= examService.findByExamId(examId);
        Exam exam= optionalExam.get();
        model.addAttribute("exam",exam);
        model.addAttribute("examId",examId);
        model.addAttribute("timeOut",exam.getTimeOut());
        List<QuestionModel> questionModels= questionService.getListQuestion(examId);
        Gson gson= new Gson();
        String result= gson.toJson(questionModels);
        Integer views= exam.getViews();
        exam.setViews(views+1);
        examService.updateExam(exam);
        model.addAttribute("dataExam",result);

        return "exam/exam-test";
    }

//    @GetMapping("/kiemtra/getData")
//    public @ResponseBody String getData(@RequestParam("examId") Integer examId){
////        List<QuestionModel> questionModels= questionService.getListQuestion(examId);
////        Gson gson= new Gson();
////        String result= gson.toJson(questionModels);
//        String result = "";
//        return result;
//    }

    private void getDefault(Model model,Principal principal){
        // get list class menu
        List<Classroom> listClass = classroomService.getAllClassroom();
        if(!listClass.isEmpty()){
            model.addAttribute("listClass",listClass);
        }

        // list lesson views
        List<Lesson> listLesson =lessonService.getListLessonOrderByViews().subList(0,5);
        model.addAttribute("listLesson",listLesson);

        //list exam views
        List<Exam> listExam =examService.getListExamOrderByViews().subList(0,5);
        model.addAttribute("listExam",listExam);

        List<News> listnewStudy= newsService.getNewsStudy().subList(0,4) ;
        model.addAttribute("listnewStudy",listnewStudy);
    }

    @GetMapping("/kiemtra/class/")
    public String getListExamByClass(Model model,Principal principal,@RequestParam("classId") Integer classId,Pageable pageable){
        getDefault(model,principal);

        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize);
        Page<Exam> pageExam = examService.getListExamByClass(classId,pageItem);
        model.addAttribute("listExam",pageExam);

        return "exam/exam-by-class";
    }
}
