package com.javateam.campProject.controller;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javateam.campProject.service.MemberService;
import com.javateam.campProject.service.SocialUserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AdminController {
	
	@Autowired
	SocialUserService socialUserService;
	
	@Autowired
	MemberService memberService;
	
	@GetMapping("/admin/changeRole")
	public String changeRole(@RequestParam("id") String id, @RequestParam("role") String role) {
		log.info("권한 변경");
		
		// 소셜 로그인 id => 숫자
		if (Pattern.matches("\\d{1,}", id) == true) { // 소셜 로그인 id => 숫자
			log.info("소셜 롤 변경: ");
			socialUserService.updateRole(Integer.parseInt(id), role);
		}
		// 자체 로그인 id => 사용자 id(문자열)
		else {
			log.info("자체 롤 변경: ");
			memberService.updateRole(id, role);
		}
			
		return "redirect:/userManagement";
	}
	
	@GetMapping("/admin/viewByRole")
	public String viewByRole(@RequestParam("role") String role, Model model) {
		log.info("viewByRole: " + role);
		
		// 전체 회원 조회
		if(role.equals("all")) {
			model.addAttribute("users", memberService.selectAllUsersWithRole());
	    	model.addAttribute("socialUsers", socialUserService.selectSocialAllMembers());
		}
		// 자체 회원 필터링 조회
		// 소셜 회원 필터링 조회
		else {
	    	model.addAttribute("users", memberService.selectAllUsersByRole(role));
	    	model.addAttribute("socialUsers", socialUserService.selectSocialAllMembersByRole(role));
		}
		
		return "userManagement";
	}
	
}
