package com.wru.onthi.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wru.onthi.entity.*;
import com.wru.onthi.services.*;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    ClassroomService classroomService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    ExamService examService;

    @Autowired
    ExamQuestionService examQuestionService;

//    @Autowired
//    QuestionUploadService questionUploadService;

    @GetMapping("/list-question-by-exam")
    public String getAllQuestion(@RequestParam("examId") Integer examId, Model model, Principal principal, Pageable pageable){
        getInfoUser(model,principal);

        Optional<Exam> optionalExam= examService.findByExamId(examId);
        Exam exam= optionalExam.get();
        String subjectName= exam.getExam_subject().getName();
        String className= exam.getExam_classroom().getClassname();

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
            return "admin/question/list-question-by-exam";
        } else {
            model.addAttribute("pageInfo", pageQuestion);
            model.addAttribute("total",totalItem);
            model.addAttribute("itemPerPage",itemPerPage);
            model.addAttribute("examId",examId);
            model.addAttribute("examCode",exam.getCodeExam());
            model.addAttribute("className",className);
            model.addAttribute("subjectName",subjectName);
            model.addAttribute("path","/question/list-question-by-exam?examId="+examId);
            return "admin/question/list-question-by-exam";
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

    @PostMapping("/update-question")
    public String updateQuestionPost(Model model, Principal principal,
                                     HttpServletRequest request,
                                     RedirectAttributes redr){

        getInfoUser(model,principal);
        Integer questionId=Integer.valueOf(request.getParameter("questionId"));
        String contentQuestion= request.getParameter("content");
        String ansA= request.getParameter("ansA");
        String ansB= request.getParameter("ansB");
        String ansC= request.getParameter("ansC");
        String ansD= request.getParameter("ansD");
        String ansCorrect=request.getParameter("ansCorrect");

        Optional<Question>optionalQuestion= questionService.findById(questionId);
        Question question= optionalQuestion.get();
        try {
            question.setContent(contentQuestion);
            question.setAnsA(ansA);
            question.setAnsB(ansB);
            question.setAnsC(ansC);
            question.setAnsD(ansD);
            question.setAnsCorrect(ansCorrect);
            questionService.updateQuestion(question);
            redr.addFlashAttribute("success","Update thông tin câu hỏi thành công");
        }catch (Exception e){
            redr.addFlashAttribute("error","Update thông tin câu hỏi thất bại");
        }
        String path="redirect:/question/update-question/"+questionId;
        return path;
    }

    @GetMapping("/add-question-by-exam")
    public String addQuestionGet(@RequestParam("examId") Integer examId,
                                 Model model, Principal principal){
        getInfoUser(model,principal);
        Optional<Exam> optionalExam= examService.findByExamId(examId);
        model.addAttribute("exam",optionalExam.get());
        model.addAttribute("exId", examId);
        return "admin/question/add-question-by-exam";
    }

    @PostMapping("/add-question-by-exam")
    public String addQuestionPost(@RequestParam("examId") Integer examId,
                                  HttpServletRequest request, Principal principal,Model model,
                                  RedirectAttributes red){
        getInfoUser(model,principal);

        Optional<Exam> optionalExam= examService.findByExamId(examId);
        Exam exam= optionalExam.get();

        String contentQuestion= request.getParameter("content");
        String ansA= request.getParameter("ansA");
        String ansB= request.getParameter("ansB");
        String ansC= request.getParameter("ansC");
        String ansD= request.getParameter("ansD");
        String ansCorrect=request.getParameter("ansCorrect");

        Question question= new Question();
        question.setContent(contentQuestion);
        question.setAnsA(ansA);
        question.setAnsB(ansB);
        question.setAnsC(ansC);
        question.setAnsD(ansD);
        question.setAnsCorrect(ansCorrect);
        Classroom classroom= exam.getExam_classroom();
        Subject subject= exam.getExam_subject();
        question.setQuestion_classroom(classroom);
        question.setQuestion_subject(subject);
        try {
            questionService.createQuestion(question);
            ExamQuestion examQuestion= new ExamQuestion();
            examQuestion.setQuestionExam(question);
            examQuestion.setExamQuestion(exam);
            examQuestionService.createExamQuestion(examQuestion);
            red.addFlashAttribute("success", "Thêm mới câu hỏi thành công");
        }
        catch (Exception e){
            red.addFlashAttribute("error", "Thêm mới câu hỏi thất bại");
        }
        String path= "redirect:/question/list-question-by-exam?examId="+ examId;
        return path;
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

    @PostMapping(value = "/upload-question-by-exam/upload" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String uploadFile(Principal principal,Model model,
                             @RequestParam("examId") Integer examId,
                             @RequestPart(name = "fileData") MultipartFile file){

        getInfoUser(model,principal);

        // Check Data
        Map<String, String> result = new HashMap<String, String>();
        String message = validateData(file);
        Gson gson = new Gson();
        if (StringUtils.isEmpty(message)) {
            result = this.processFile(file,examId);
        } else {
            result.put("STATUS", "1");
            result.put("MESSAGE", message);
        }
        return gson.toJson(result);
    }

    private Map<String, String> processFile(MultipartFile file, Integer examId) {
        Map<String, String> result = new HashMap<>();
        int iSuccess = 0;
        String flName = file.getOriginalFilename();
        InputStream inp = null;
        String[] flPath = flName.split("[.]");
        String flExtension = flPath[flPath.length - 1].toUpperCase();
        Workbook wbFile = null;
        try {
            inp = file.getInputStream();
            if ("XLSX".equals(flExtension)) {
                wbFile = new XSSFWorkbook(file.getInputStream());
            } else {
                wbFile = new HSSFWorkbook(file.getInputStream());
            }
            Sheet sheet = wbFile.getSheetAt(0);
            Iterator<Row> rows = sheet.rowIterator();
            // Skip the first row
            rows.next();
            while (rows.hasNext()) {
                if (insertQuestion(rows.next(), examId)) {
                    iSuccess++;
                }
            }
        } catch (IOException e) {
           e.printStackTrace();
        } finally {
            if (inp != null) {
                try {
                    inp.close();
                } catch (IOException e) {
                   e.printStackTrace();
                }
            }
        }
        if (iSuccess == 0) {
            result.put("STATUS", "1");
            result.put("MESSAGE", "Cannot create the detail data.");
        }
        result.put("NUM_SUCCESS", iSuccess + "");
        return result;
    }

    private boolean insertQuestion(Row row, Integer examId) {
        Optional<Exam> optionalExam= examService.findByExamId(examId);
        Exam exam = optionalExam.get();
        Classroom classroom = exam.getExam_classroom();
        Subject subject = exam.getExam_subject();

        try {
            Question question= new Question();
            Cell cell;
            CellType cellType;
            String temp = null;

            cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cellType = cell.getCellType();
            if (CellType.STRING.equals(cellType)) {
                temp = StringUtils.trim(cell.getStringCellValue());
                question.setContent(temp);
            }

            cell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cellType = cell.getCellType();
            if (CellType.STRING.equals(cellType)) {
                temp = StringUtils.trim(cell.getStringCellValue());
                question.setAnsA(temp);
            }

            cell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cellType = cell.getCellType();
            if (CellType.STRING.equals(cellType)) {
                temp = StringUtils.trim(cell.getStringCellValue());
                question.setAnsB(temp);
            }

            cell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cellType = cell.getCellType();
            if (CellType.STRING.equals(cellType)) {
                temp = StringUtils.trim(cell.getStringCellValue());
                question.setAnsC(temp);
            }

            cell = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cellType = cell.getCellType();
            if (CellType.STRING.equals(cellType)) {
                temp = StringUtils.trim(cell.getStringCellValue());
                question.setAnsD(temp);
            }

            // cmnd
            cell = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cellType = cell.getCellType();
            temp = null;
            if (CellType.NUMERIC.equals(cellType)) {
                temp = new BigDecimal(cell.getNumericCellValue()).toString();
            } else if (CellType.STRING.equals(cellType)) {
                temp = StringUtils.trim(cell.getStringCellValue());
            }
            if (StringUtils.isNotEmpty(temp)) {
                question.setAnsCorrect(temp);
            }

            question.setQuestion_classroom(classroom);
            question.setQuestion_subject(subject);
            questionService.createQuestion(question);

            ExamQuestion examQuestion= new ExamQuestion();
            examQuestion.setQuestionExam(question);
            examQuestion.setExamQuestion(exam);
            examQuestionService.createExamQuestion(examQuestion);

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private String validateData(MultipartFile file) {
        StringBuilder builderMessage = new StringBuilder();
        if (file == null) {
            builderMessage.append("File không đúng định dạng xlsx hoặc xls");
        } else {
            String flName = file.getOriginalFilename();
            String[] flPath = flName.split("[.]");
            String flExtension = flPath[flPath.length - 1].toUpperCase();
            if (!"XLSX".equals(flExtension) && !"XLS".equals(flExtension)) {
                builderMessage.append("File không đúng định dạng xlsx hoặc xls");
            }
        }
        return builderMessage.toString();
    }

    @GetMapping("/search-question")
    public String chooseQuestion(Model model, Principal principal){
        getInfoUser(model,principal);

        //get AllClassroom
        List<Classroom> listClass= classroomService.getAllClassroom();
        model.addAttribute("listClass",listClass);

        //get list Subject
        List<Subject> listSubject= subjectService.getlistSubject();
        model.addAttribute("listSubject", listSubject);
        return "admin/question/search-question";
    }

    @GetMapping("/list-question-by-subject-class")
    public String getListQuestionBySubjectAndClass(Model model,Principal principal,
                                                   @RequestParam("subjectId") Integer subjectId,
                                                   @RequestParam("classId") Integer classId,
                                                   Pageable pageable){
        getInfoUser(model,principal);

        Optional<Classroom> optional= classroomService.findById(classId);
        Classroom classroom= optional.get();
        Optional<Subject> optionalSubject= subjectService.findBySubjectId(subjectId);
        Subject subject= optionalSubject.get();

        int pageNumber = pageable.getPageNumber();
        int pageSize= 8;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<Question> pageQuestion= questionService.getPageQuestionBySubjectAndClass(subjectId,classId,pageItem);
        int totalItem = (int) pageQuestion.getTotalElements();
        int itemPerPage= pageSize * (pageNumber+1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }
        if(pageQuestion == null){
            model.addAttribute("error","Danh sách câu hỏi trống.");
            return "admin/question/list-question-by-subject-class";
        } else {
            model.addAttribute("pageInfo", pageQuestion);
            model.addAttribute("total",totalItem);
            model.addAttribute("itemPerPage",itemPerPage);
            model.addAttribute("className",classroom.getClassname());
            model.addAttribute("subjectName",subject.getName());
            model.addAttribute("classId",classId);
            model.addAttribute("subjectId",subjectId);
            model.addAttribute("path","/question/list-question-by-subject-class?subjectId="+ subjectId+"&classId="+classId);
            return "admin/question/list-question-by-subject-class";
        }
    }

    @GetMapping("/remove-question-from-exam/{questionId}/{examId}")
    public String removeQuestionFromExam(Model model, Principal principal,RedirectAttributes redr,
                                         @PathVariable("questionId") Integer questionId,
                                         @PathVariable("examId") Integer examId){

        getInfoUser(model,principal);
        try {
            examQuestionService.removeQuestionFromExam(questionId,examId);
            redr.addFlashAttribute("success","Xóa thành công câu hỏi khỏi đề thi.");
        }catch (Exception ex){
            redr.addFlashAttribute("error","Xóa câu hỏi khỏi đề thi thất bại");
        }
        String path= "redirect:/question/list-question-by-exam?examId="+examId;
        return path;
    }

    @GetMapping("/delete-question")
    public String deleteQuestion(@RequestParam("questionId") Integer id,
                                 @RequestParam("subjectId") Integer subjectId,
                                 @RequestParam("classId") Integer classId,
                                 Model model,Principal principal,
                                 RedirectAttributes red){
        getInfoUser(model,principal);
        Optional<Question> optionalQuestion= questionService.findById(id);
        Question question= optionalQuestion.get();
        try {
            questionService.deleteQuestion(question);
            red.addFlashAttribute("success","Xóa câu hỏi thành công.");
        }catch (Exception e){
            e.printStackTrace();
            red.addFlashAttribute("success","Xóa câu hỏi thất bại.");
        }
        String path="redirect:/question/list-question-by-subject-class?subjectId="+subjectId+"&classId="+classId;
        return path;
    }

    @GetMapping("/add-question-from-db")
    public String addQuestionFromDB(Model model,Principal principal,
                                    @RequestParam("examId") Integer examId) {
        getInfoUser(model,principal);
        Optional<Exam> optionalExam= examService.findByExamId(examId);
        Exam exam= optionalExam.get();
        Integer subjectId= exam.getExam_subject().getId();
        Integer classId= exam.getExam_classroom().getId();
        List<Question> ListQuestionBySubjectAndClass= questionService.getListQuestionBySubjectAndClass(subjectId,classId);

        GsonBuilder gsonBuilder = new GsonBuilder();
        AdapterClass adapterClass= new AdapterClass();
        Gson gson = gsonBuilder.registerTypeAdapter(Question.class,adapterClass).create();
        String jsonArray=gson.toJson(ListQuestionBySubjectAndClass);
        model.addAttribute("data",jsonArray);
        model.addAttribute("examId",examId);

        return "admin/question/add-question-from-db";
    }

    @RequestMapping(value = "/insert-question-to-exam",method = RequestMethod.POST)
    public @ResponseBody String addQuestionToExam(@RequestParam("examId") String examId,
                                   @RequestParam("array_data") String data) throws JsonProcessingException {
        //arr question id
        int[] questionId = new ObjectMapper().readValue(data, int[].class);

        List<Integer> listQuestionIdRequest= new ArrayList<>();
        for(int i=0; i<questionId.length;i++){
            listQuestionIdRequest.add(questionId[i]);
        }
        Optional<Exam> optionalExam= examService.findByExamId(Integer.valueOf(examId));
        Exam exam= optionalExam.get();
        List<ExamQuestion> listExamQuestion= examQuestionService.getListExamQuestionByExamId(Integer.valueOf(examId));

        List<Integer> exq= new ArrayList<>();// luu id question exist
        List<Integer> eqExist= new ArrayList<>();
        if(listExamQuestion.size()>0){
            // get list id question exxit
            for(int i = 0; i < listExamQuestion.size();i++){
                exq.add(listExamQuestion.get(i).getQuestionExam().getId());
                Collections.sort(exq);
            }
            for(Integer i : listQuestionIdRequest){
                if(exq.contains(i)){
                    eqExist.add(i);
                }
                else {
                    ExamQuestion examQuestion= new ExamQuestion();
                    examQuestion.setExamQuestion(exam);
                    Optional<Question> optionalQuestion = questionService.findById(i);
                    examQuestion.setQuestionExam(optionalQuestion.get());
                    examQuestionService.createExamQuestion(examQuestion);
                }
            }
        }else {
            for(Integer i : listQuestionIdRequest){
                ExamQuestion examQuestion= new ExamQuestion();
                examQuestion.setExamQuestion(exam);
                Optional<Question> optionalQuestion = questionService.findById(i);
                examQuestion.setQuestionExam(optionalQuestion.get());
                examQuestionService.createExamQuestion(examQuestion);
            }
        }

        return "OK";
    }
}
