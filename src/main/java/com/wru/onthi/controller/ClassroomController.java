package com.wru.onthi.controller;

import com.wru.onthi.entity.Classroom;
import com.wru.onthi.services.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ClassroomController {

    @Autowired
    ClassroomService classroomService;




}
