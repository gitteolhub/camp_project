package com.javateam.campProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CampGroundManagementController {

    @GetMapping("/campGroundManagement")
    public String campGroundManagement() {
        return "campGroundManagement"; // campGroundManagement.html 파일을 반환
    }
}