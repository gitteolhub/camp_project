package com.javateam.campProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageGeneralUserController {

    @GetMapping("/myPageGeneralUser")
    public String myPageGeneralUser() {
        return "myPageGeneralUser"; // myPageGeneralUser.html 파일을 반환
    }
}