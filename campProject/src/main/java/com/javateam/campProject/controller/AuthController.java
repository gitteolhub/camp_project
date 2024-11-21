package com.javateam.campProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.javateam.campProject.domain.MemberVO;
import com.javateam.campProject.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AuthController {

	@Autowired
	MemberService memberService;

	// 홈 페이지로 redirection (return문 >> 경로)
	@RequestMapping("/")
	public String root() {

		log.info("[root]");
		return "redirect:/choiceJoin";
	}

    // 회원가입 메서드
    @GetMapping("/choiceJoin")
    public String choiceJoin() {
    	return "choiceJoin";
    }
    @GetMapping("/userJoin")
    public String userJoin(Model model) {
    	MemberVO memberVO = new MemberVO();
    	memberVO.setMemberType("user");
    	model.addAttribute("memberDTO", memberVO);
    	return "memberJoin";
    }
    @GetMapping("/ceoJoin")
    public String ceoJoin(Model model) {
    	MemberVO memberVO = new MemberVO();
		memberVO.setMemberType("ceo");
		model.addAttribute("memberDTO", memberVO);
    	return "memberJoin";
    }
	// 로그인 페이지를 반환
	@GetMapping("/loginForm")
	public String login(ModelMap model) {
		log.info("[loginForm]");
		return "loginForm";
	}

	// 로그아웃 처리 메서드
	@GetMapping("/logoutProc")
	public String logout(Model model, HttpServletRequest request, HttpServletResponse response) {
		log.info("[logout]");

		// 혅대 인증 정보
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.info("[auth: {}]", auth);

		// 인증 정보가 존재하는 경우 로그아웃
		if(auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "logout";
	}

	// 로그인 오류 처리 메서드
	@GetMapping("/loginError")
	public String loginError(Model model, HttpSession httpSession, RedirectAttributes redirectAttributes) {

		// 마지막 보안 예외 가져오기
		Exception secuSess = (Exception)httpSession.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
		log.info("[인증 오류 {}]", secuSess.getMessage());

		// 오류 flag 추가
		redirectAttributes.addAttribute("error", "true");

		// 오류 메서드 추가
		redirectAttributes.addAttribute("mag", secuSess.getMessage());

		return "redirect:/loginForm";
	}

}
