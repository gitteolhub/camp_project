package com.javateam.campProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserManagementController {

    @GetMapping("/userManagement")
    public String userManagement() {
        return "userManagement"; // userManagement.html 파일을 반환
    }
}