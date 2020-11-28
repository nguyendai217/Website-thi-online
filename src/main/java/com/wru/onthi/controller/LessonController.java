package com.wru.onthi.controller;

import com.wru.onthi.entity.Classroom;
import com.wru.onthi.entity.Exam;
import com.wru.onthi.entity.Lesson;
import com.wru.onthi.entity.Subject;
import com.wru.onthi.repository.ExamRepository;
import com.wru.onthi.services.ClassroomService;
import com.wru.onthi.services.ExamService;
import com.wru.onthi.services.LessonService;
import com.wru.onthi.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class LessonController {

    @Autowired
    LessonService lessonService;

    @Autowired
    ClassroomService classroomService;

    @Autowired
    ExamService examService;

    @Autowired
    SubjectService subjectService;


    @GetMapping("/baihoc")
    public String getListLesson(Model model,
                                @RequestParam("class_id") Integer class_id,
                                @RequestParam("subject_id") Integer subject_id){
        genDefault(model);

        Optional<Classroom> optional = classroomService.findById(class_id);
        Classroom classroom= optional.get();
        Optional<Subject> optionalSubject=subjectService.findBySubjectId(subject_id);
        Subject subject= optionalSubject.get();
        List<Lesson> listLessons= lessonService.getListLessonByClassAndSubject(class_id,subject_id);
        if(listLessons.size()>0){
            model.addAttribute("listLessonByClass",listLessons);
        } else {
            model.addAttribute("emptyLesson","listEmpty");
        }
        model.addAttribute("className",classroom.getClassname());
        model.addAttribute("subjectName",subject.getName());
        return "lesson/list-lesson";
    }
    @GetMapping("/noidungbaihoc")
    public String getContentLesson(Model model, @RequestParam("lessonId") Integer lessonId){
       genDefault(model);
       Lesson lesson= lessonService.getContentLesson(lessonId);

       // tang views khi click vao hoc
       Integer views= lesson.getViews();
       lesson.setViews(views+1);
       lessonService.updateLesson(lesson);
       model.addAttribute("lessonContent",lesson);
       return "lesson/content_lesson";
    }

    private void genDefault(Model model){
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
    }


}
