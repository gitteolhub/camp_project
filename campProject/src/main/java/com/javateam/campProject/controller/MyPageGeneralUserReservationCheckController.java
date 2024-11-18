package com.javateam.campProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageGeneralUserReservationCheckController {

    @GetMapping("/myPageGeneralUserReservationCheck")
    public String myPageGeneralUserReservationCheck() {
        return "myPageGeneralUserReservationCheck"; // myPageGeneralUserReservationCheck.html 파일을 반환
    }
}