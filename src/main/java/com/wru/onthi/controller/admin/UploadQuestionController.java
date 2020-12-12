package com.wru.onthi.controller.admin;

import com.google.gson.Gson;
import com.wru.onthi.entity.*;
import com.wru.onthi.services.ClassroomService;
import com.wru.onthi.services.QuestionService;
import com.wru.onthi.services.SubjectService;
import com.wru.onthi.services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@Controller
public class UploadQuestionController {

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    ClassroomService classroomService;

    @Autowired
    SubjectService subjectService;

    @GetMapping("/question/add-question-by-subject-class")
    public String addQuestionBySubjectClass(Model model, Principal principal,
                                 @RequestParam("subjectId") Integer subjectId,
                                 @RequestParam("classId") Integer classId){
        getInfoUser(model,principal);
        Optional<Classroom> optionalClassroom= classroomService.findById(classId);
        Classroom classroom= optionalClassroom.get();
        Optional<Subject> optionalSubject= subjectService.findBySubjectId(subjectId);
        Subject subject= optionalSubject.get();
        model.addAttribute("subject",subject);
        model.addAttribute("classroom",classroom);
        model.addAttribute("subjectId",subjectId);
        model.addAttribute("classId",classId);
        return "admin/question/add-question-by-subject-class";
    }

    @PostMapping("/question/add-question-by-subject-class")
    public String addQuestionBySubjectClassPost(Model model, Principal principal,
                                                @RequestParam("subjectId") Integer subjectId,
                                                @RequestParam("classId") Integer classId,
                                                HttpServletRequest request, RedirectAttributes red){
        getInfoUser(model,principal);

        Optional<Classroom> optionalClassroom= classroomService.findById(classId);
        Classroom classroom= optionalClassroom.get();
        Optional<Subject> optionalSubject= subjectService.findBySubjectId(subjectId);
        Subject subject= optionalSubject.get();

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
        question.setQuestion_subject(subject);
        question.setQuestion_classroom(classroom);

        try {
            questionService.createQuestion(question);
            red.addFlashAttribute("success", "Thêm mới câu hỏi thành công");

        }catch (Exception e){
            e.printStackTrace();
            red.addFlashAttribute("error", "Thêm mới câu hỏi thất bại.");
        }
        String path= "redirect:/question/list-question-by-subject-class?subjectId="+subjectId+"&classId="+classId;
        return path;
    }

    @PostMapping(value = "/question/upload-question-by-subject-class/upload" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String uploadFile(Principal principal,Model model,
                             @RequestParam("subjectId") Integer subjectId,
                             @RequestParam("classId") Integer classId,
                             @RequestPart(name = "fileData") MultipartFile file){

        getInfoUser(model,principal);

        // Check Data
        Map<String, String> result = new HashMap<String, String>();
        String message = validateData(file);
        Gson gson = new Gson();
        if (StringUtils.isEmpty(message)) {
            result = this.processFile(file,subjectId,classId);
        } else {
            result.put("STATUS", "1");
            result.put("MESSAGE", message);
        }
        return gson.toJson(result);
    }
//
    private Map<String, String> processFile(MultipartFile file, Integer subjectId,Integer classId) {
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
                if (insertQuestion(rows.next(),subjectId,classId)) {
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

    private boolean insertQuestion(Row row, Integer subjectId,Integer classId) {

        Optional<Subject> optionalSubject= subjectService.findBySubjectId(subjectId);
        Optional<Classroom> optionalClassroom= classroomService.findById(classId);

        try {
            Question question= new Question();
            Cell cell;
            CellType cellType;
            String temp = null;

            // content question
            cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cellType = cell.getCellType();
            if (CellType.STRING.equals(cellType)) {
                temp = StringUtils.trim(cell.getStringCellValue());
                question.setContent(temp);
            }

            //answer A
            cell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cellType = cell.getCellType();
            if (CellType.STRING.equals(cellType)) {
                temp = StringUtils.trim(cell.getStringCellValue());
                question.setAnsA(temp);
            }

            //answer B
            cell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cellType = cell.getCellType();
            if (CellType.STRING.equals(cellType)) {
                temp = StringUtils.trim(cell.getStringCellValue());
                question.setAnsB(temp);
            }

            //answer C
            cell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cellType = cell.getCellType();
            if (CellType.STRING.equals(cellType)) {
                temp = StringUtils.trim(cell.getStringCellValue());
                question.setAnsC(temp);
            }

            //answer D
            cell = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cellType = cell.getCellType();
            if (CellType.STRING.equals(cellType)) {
                temp = StringUtils.trim(cell.getStringCellValue());
                question.setAnsD(temp);
            }

            //answer Correct
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

            question.setQuestion_classroom(optionalClassroom.get());
            question.setQuestion_subject(optionalSubject.get());
            questionService.createQuestion(question);

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
