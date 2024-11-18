package com.javateam.campProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageGeneralUserModificationController {

    @GetMapping("/MyPageGeneralUserModification")
    public String MyPageGeneralUserModification() {
        return "MyPageGeneralUserModification"; // myPageGeneralUser.html 파일을 반환
    }
}