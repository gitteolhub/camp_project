package com.javateam.campProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javateam.campProject.domain.SessionUser;
import com.javateam.campProject.service.MemberService;
import com.javateam.campProject.service.SocialUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MemberDeleteController {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	SocialUserService socialUserService;
	
	@GetMapping("/deleteMember")
	public String deleteMember(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("deleteMember: " + id);
		
		// 회원 role 및 정보 삭제
		if (memberService.deleteMember(id) == true) {
			// 세션 종료 후 홈으로 이동
			// 현재 인증 정보
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			log.info("[auth: {}]", auth);

			// 인증 정보가 존재하는 경우 로그아웃
			if(auth != null) {
				new SecurityContextLogoutHandler().logout(request, response, auth);
			}
					
		}
		// 삭제 실패 시
		else {
			model.addAttribute("errMsg", "회원 탈퇴 처리에 실패했습니다");
			model.addAttribute("movePage", "/myPageModification");
			return "/error";
		}
		
		return "redirect:/searchRecomCamp";
	}

	@GetMapping("/deleteSocialMember")
	public String deleteSocialMember(@RequestParam("id") int id, HttpServletRequest request,
		   HttpServletResponse response, Model model, HttpSession httpSession) {
		
		log.info("deleteSocialMember: " + id);
		
		SessionUser sessionUser = (SessionUser)httpSession.getAttribute("socialUser");
		String authVendor = sessionUser.getAuthVendor();
		
		if (socialUserService.deleteSocialUser(id) == true) {
			// 로그아웃 처리
			// 네이버
			// 구글
			model.addAttribute("authVendor", authVendor);
			
			return "/socialLogout";
		}
		else {
			model.addAttribute("errMsg", "회원 탈퇴 처리에 실패했습니다");
			model.addAttribute("movePage", "/myPageModification");
			return "/error";
		}
	
//		return "redirect:/searchRecomCamp";
	}
}
