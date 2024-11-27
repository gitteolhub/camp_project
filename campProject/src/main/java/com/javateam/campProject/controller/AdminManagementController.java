package com.javateam.campProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminManagementController {

    @GetMapping("/adminManagement")
    public String adminManagement() {
        return "adminManagement"; // adminManagement.html 파일을 반환
    }
}