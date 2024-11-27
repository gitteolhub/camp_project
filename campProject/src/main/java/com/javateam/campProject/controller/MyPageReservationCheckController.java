package com.javateam.campProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageReservationCheckController {

    @GetMapping("/myPageReservationCheck")
    public String myPageReservationCheck() {
        return "myPageReservationCheck"; // myPageReservationCheck.html 파일을 반환
    }
}