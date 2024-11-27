package com.javateam.campProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageModificationController {

    @GetMapping("/myPageModification")
    public String myPageModification() {
        return "myPageModification"; // myPageModification.html 파일을 반환
    }
}