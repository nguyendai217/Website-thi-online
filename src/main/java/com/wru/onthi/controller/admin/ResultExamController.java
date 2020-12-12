package com.wru.onthi.controller.admin;

import com.wru.onthi.entity.News;
import com.wru.onthi.entity.Result;
import com.wru.onthi.entity.User;
import com.wru.onthi.services.ResultService;
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
@RequestMapping("/result")
public class ResultExamController {

    @Autowired
    UserService userService;

    @Autowired
    ResultService resultService;

    @GetMapping("/list-result")
    public String getListResultExam(Model model, Principal principal,
                                    String username, String examCode,
                                    Pageable pageable){

        getInfoUser(model,principal);

        int pageNumber = pageable.getPageNumber();
        int pageSize=5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize);
        Page<Result> resultList = null;
        if((username== null || username =="") && (examCode== null || examCode=="")){
            resultList =resultService.getListResultExam(pageItem);
        }else {
//            String us= "%" + username + "%";
//            String ex= "%"+ examCode +"%";
            resultList= resultService.searchResultExam(username,examCode,pageItem);
            model.addAttribute("us",username);
            model.addAttribute("ec",examCode);
        }
        int totalItem = (int) resultList.getTotalElements();
        int itemPerPage= pageSize * (pageNumber +1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }
        model.addAttribute("pageInfo", resultList);
        model.addAttribute("total",totalItem);
        model.addAttribute("itemPerPage",itemPerPage);
        model.addAttribute("path","/result/list-result");
        if(username==null){
            username="";
        }
        if(examCode== null){
            examCode="";
        }
        model.addAttribute("condition",("username="+ username +"&examCode="+examCode));
        return "admin/result/list-result";
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

    @GetMapping("/delete-result/{resultId}")
    public String deleteResult(Model model, Principal principal,
                               @PathVariable("resultId") Integer resultId,
                               RedirectAttributes redr){
        getInfoUser(model,principal);
        try {
            Optional<Result> optionalResult= resultService.findById(resultId);
            Result result= optionalResult.get();
            resultService.deleteResult(result);
            redr.addFlashAttribute("success","Xóa kết quả thi thành công");
        }catch (Exception e){
            redr.addFlashAttribute("error","Xóa kết quả thi thất bại");
        }
        return "redirect:/result/list-result";
    }


}
