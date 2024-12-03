package com.javateam.campProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.javateam.campProject.service.MemberService;
import com.javateam.campProject.service.SocialUserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserManagementController {
	
	@Autowired MemberService memberService;
	@Autowired SocialUserService socialUserService;

    @GetMapping("/userManagement")
    public String userManagement(Model model) {
    	log.info("userManagement");
    	model.addAttribute("users", memberService.selectAllUsersWithRole());
    	model.addAttribute("socialUsers", socialUserService.selectSocialAllMembers());
    	
        return "userManagement"; // userManagement.html 파일을 반환
    }
}