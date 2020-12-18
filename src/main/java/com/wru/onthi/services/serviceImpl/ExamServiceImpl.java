package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.Exam;
import com.wru.onthi.repository.ExamRepository;
import com.wru.onthi.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    ExamRepository examRepository;

    @Override
    public Optional<Exam> findByExamId(Integer examId) {
        return examRepository.findById(examId);
    }

    @Override
    public List<Exam> getListExam() {
        return examRepository.findAll();
    }

    @Override
    public List<Exam> getListExamOrderByViews() {
        return examRepository.getlistExamOrderByViews();
    }

    @Override
    public Page<Exam> getAllListExam(Pageable pageable) {
        return examRepository.findAll(pageable);
    }

    @Override
    public List<Exam> getListExamBySubjectAndClass(Integer subjectId, Integer classId) {
        return examRepository.getListExamBySubjectAndClass(subjectId,classId);
    }

    @Override
    public Exam updateExam(Exam exam) {
        return examRepository.save(exam);
    }

    @Override
    public void createExam(Exam exam) {
        examRepository.save(exam);
    }

    @Override
    public void disableExam(Integer examId) {
        examRepository.disableExam(examId);
    }

    @Override
    public void deleteExam(Exam exam) {
        examRepository.delete(exam);
    }

    @Override
    public Page<Exam> searchExam(String examCode, String subjectId, String classId,Pageable pageable) {
        if(examCode==""){
            examCode = null;
        }
        if(subjectId ==""){
            subjectId = null;
        }
        if(classId== ""){
            classId = null;
        }
        return examRepository.searchExam(examCode,subjectId,classId,pageable);
    }

    @Override
    public Page<Exam> getListExamByClass(Integer classId, Pageable pageable) {
        return examRepository.getListExamByClass(classId,pageable);
    }

    @Override
    public void updateStatus(Integer examId, Integer status) {
        examRepository.updateStatus(examId,status);
    }

    @Override
    public List<Exam> getListExamNew() {
        return examRepository.getlistExamNew();
    }

    @Override
    public List<Exam> getlistExamByClass(Integer classId) {
        return examRepository.getlistExamByClass(classId);
    }

    @Override
    public List<Exam> getlistExamOrdedByIDDesc() {
        return examRepository.getlistExamOrderByIdDesc();
    }

    @Override
    public long countExam() {
        return examRepository.count();
    }
}
