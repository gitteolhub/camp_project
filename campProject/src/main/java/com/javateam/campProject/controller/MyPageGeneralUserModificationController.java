package com.javateam.campProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageGeneralUserModificationController {

    @GetMapping("/myPageGeneralUserModification")
    public String myPageGeneralUserModification() {
        return "myPageGeneralUserModification"; // myPageGeneralUserModification.html 파일을 반환
    }
}