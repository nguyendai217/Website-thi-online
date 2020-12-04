package com.wru.onthi.controller.admin;

import com.wru.onthi.entity.Question;
import com.wru.onthi.entity.User;
import com.wru.onthi.model.QuestionModel;
import com.wru.onthi.services.QuestionService;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @GetMapping("/list-question")
    public String getAllQuestion(@RequestParam("examId") Integer examId, Model model, Principal principal, org.springframework.data.domain.Pageable pageable){
        getInfoUser(model,principal);

        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<Question> pageQuestion= null;
        pageQuestion= questionService.getPageQuestionByExamId(examId,pageItem);

        int totalItem = (int) pageQuestion.getTotalElements();
        int itemPerPage= pageSize * (pageNumber+1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }

        if(pageQuestion == null){
            model.addAttribute("error","Danh sách câu hỏi trống.");
            return "admin/question/list-question";
        } else {
            model.addAttribute("pageInfo", pageQuestion);
            model.addAttribute("total",totalItem);
            model.addAttribute("itemPerPage",itemPerPage);
            model.addAttribute("examId",examId);
            model.addAttribute("path","/question/list-question?examId="+examId);
            return "admin/question/list-question";
        }
    }

    @GetMapping("/update-question/{questionId}")
    public String updateQuestionGet(Model model, Principal principal, @PathVariable("questionId") Integer questionId){
        getInfoUser(model,principal);
        Optional<Question> optionalQuestion= questionService.findById(questionId);
        Question question= optionalQuestion.get();
        model.addAttribute("qs",question);
        return "admin/question/update-question";
    }

    @GetMapping("/add-question")
    public String addQuestionGet(@RequestParam("examId") Integer examId,
                                 Model model, Principal principal, Pageable pageable){
        getInfoUser(model,principal);

        return "admin/question/add-question";
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
