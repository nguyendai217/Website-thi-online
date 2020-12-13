package com.wru.onthi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.wru.onthi.entity.*;
import com.wru.onthi.model.QuestionModel;
import com.wru.onthi.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Date;
import java.util.List;
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

    @Autowired
    ResultService resultService;

    @Autowired
    UserService userService;


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
        List<Exam> listExam= examService.getListExamNew().subList(0,4);
        model.addAttribute("listExam",listExam);
        model.addAttribute("subjectId",subjectId);
        model.addAttribute("listClass",getListClassBySubject);
        return "exam/list-class-bySubject";
    }

    //get list exam by subject

    @GetMapping("/kiemtra/list-exam")
    public String listExam(Model model,Principal principal,
                           @RequestParam("subjectId") Integer subjectId,
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
        User user = userService.findUserByName(principal.getName());
        Optional<Exam> examOptional = examService.findByExamId(examId);
        Exam examDetail = examOptional.get();
        Exam exam= optionalExam.get();
        Date dateTest = new Date();
        Subject subject= exam.getExam_subject();
        String subjectName= subject.getName();
        model.addAttribute("subjectName",subjectName);
        model.addAttribute("exam",exam);
        model.addAttribute("examId",examId);
        model.addAttribute("timeOut",exam.getTimeOut());

        Optional<Result> testOptional = resultService.checkResultExist(exam.getTimeOut(), user.getId(), examId, 0);
        Result test = null;
        if (testOptional.isPresent() == false) {
            test = new Result();
        } else {
            test = testOptional.get();
            if(test.getStatus() == 1) {
                return "redirect:/history?testId="+test.getId();
            }
        }

        test.setCreatedAt(dateTest);
        test.setStatus(0);
        test.setUserResult(user);
        test.setExam(examDetail);
        resultService.save(test);
        List<QuestionModel> questionModels= questionService.getListQuestion(examId,false);
        Gson gson= new Gson();
        String result= gson.toJson(questionModels);
        Integer views= exam.getViews();
        exam.setViews(views+1);
        examService.updateExam(exam);
        model.addAttribute("dataExam",result);
        model.addAttribute("testId",test.getId());
        return "exam/exam-test";
    }

    @RequestMapping(path = "/kiemtra-process-result", method = RequestMethod.POST)
    public @ResponseBody String processResult(Principal principal,
                                              HttpServletResponse httpRep,
                                              @RequestParam(value = "dataJson", defaultValue = "") String dataJson,
                                              @RequestParam(value = "examId", defaultValue = "") Integer examId,
                                              @RequestParam(value = "testId", defaultValue = "") Integer testId
    ) throws JsonMappingException, JsonProcessingException {

        Gson gson= new Gson();
        int scores = 0;
        Double formatScores = null;

        int[] array = new ObjectMapper().readValue(dataJson, int[].class);
        List<QuestionModel> questionModels = questionService.getListQuestion(examId,true);

        for (int i = 0; i < questionModels.size(); i++) {
            String ansCorrect = questionModels.get(i).getAnsCorrect();
            if (Integer.valueOf(ansCorrect) == array[i]) {
                scores++;
            }
        }

        formatScores =  (double)Math.round(1000 * (double)scores / questionModels.size()) / 100;
        Optional<Result> resultOptional =  resultService.findById(testId);
        Result result =  resultOptional.get();
        result.setDetail(gson.toJson(array));
        result.setScore(formatScores);
        result.setStatus(1);
        resultService.save(result);

        httpRep.setStatus(HttpServletResponse.SC_OK);

        return "{\"code\":\"200\"}";
    }

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
    public String getListExamByClass(Model model,Principal principal,@RequestParam("classId") Integer classId,
                                     Pageable pageable){
        getDefault(model,principal);
        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize);
        Page<Exam> pageExam = examService.getListExamByClass(classId,pageItem);
        model.addAttribute("listExam",pageExam);

        return "exam/exam-by-class";
    }

    @GetMapping("/history")
    public String getHistory(Model model,Principal principal, @RequestParam("testId") Integer testId){
        Optional<Result> resultOptional =  resultService.findById(testId);
        Result result =  resultOptional.get();
        Exam exam = result.getExam();
        getDefault(model,principal);
        List<QuestionModel> questionModels= questionService.getListQuestion(exam.getId(),true);
        Gson gson= new Gson();
        String dateJson = gson.toJson(questionModels);

        Subject subject= exam.getExam_subject();
        String subjectName= subject.getName();
        model.addAttribute("dataExam", dateJson);
        model.addAttribute("resultDetail", result.getDetail());
        model.addAttribute("resultData", result);
        model.addAttribute("exam",exam);
        model.addAttribute("testId",result.getId());
        model.addAttribute("examId",exam.getId());
        model.addAttribute("timeOut",exam.getTimeOut());
        model.addAttribute("subjectName",subjectName);

        return "exam/history-test";
    }
}
