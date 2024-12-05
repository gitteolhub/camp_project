package com.javateam.campProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.javateam.campProject.service.CampReservationService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CampGroundManagementController {
	
	@Autowired
	CampReservationService campReservationService;

    @GetMapping("/campGroundManagement")
    public String campGroundManagement(Model model) {
    	log.info("campGroundManagement");
    	model.addAttribute("reservationList", campReservationService.selectAllReservationList());
    	
        return "campGroundManagement"; // campGroundManagement.html 파일을 반환
    }
}