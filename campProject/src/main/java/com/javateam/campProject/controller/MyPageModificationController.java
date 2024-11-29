package com.javateam.campProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.javateam.campProject.domain.CustomUser;
import com.javateam.campProject.domain.MemberUpdateDTO;
import com.javateam.campProject.domain.MemberVO;
import com.javateam.campProject.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MyPageModificationController {
	
	@Autowired
	MemberService memberService;

    @GetMapping("/myPageModification")
    public String myPageModification(Model model) {  // Model 인자를 이용해 페이지 타이틀 설정
    	log.info("회원정보 수정");
    	
    	// Spring Security Pricipal(Session) 조회
		Object principal = SecurityContextHolder.getContext()
												.getAuthentication()
												.getPrincipal();

		CustomUser customUser = (CustomUser)principal;

		log.info("[principal] : {}", principal);
		log.info("[customUser.getUsername()][id] : {}", customUser.getUsername());	// 로그인 아이디
		
		String id = customUser.getUsername();
		
		MemberVO memberVO = memberService.selectMemberById(id);

		if(memberVO == null) {
			// 회원정보가 없는 경우, 에러 처리
			model.addAttribute("errorMsg", "회원 정보가 존재하지 않습니다.");
			return "/error/error";

		} else {

			MemberUpdateDTO memberUpdateDTO = new MemberUpdateDTO(memberVO);
			model.addAttribute("memberUpdateDTO", memberUpdateDTO);
		}

    	model.addAttribute("headerName", "마이페이지 회원정보 수정");  // [[${headerName}]] 부분에 들어갈 페이지 타이틀
        return "myPageModification"; // myPageModification.html 파일을 반환
    }

    @PostMapping("/member/updateProc")
    public String updateProc(@ModelAttribute("memberUpdateDTO") MemberUpdateDTO memberUpdateDTO, Model model) {
    	log.info("회원정보 수정 처리: {}", memberUpdateDTO);
    	
    	String msg      = "";
		String movePath = "";

		boolean blRetVal = memberService.updateMember(memberUpdateDTO);

		if(blRetVal == true) {

			msg      = "회원정보를 수정했습니다.";
//			movePath = "myPageModification";

		} else {

			msg      = "회원정보 수정에 실패했습니다.";
//			movePath = "myPageModification";
		}
		log.info("[updateProc][result] : {}", msg);
//		redirectAttributes.addAttribute("msg", msg);

		model.addAttribute("msg", msg);
    	model.addAttribute("memberUpdateDTO", memberUpdateDTO);
    	model.addAttribute("headerName", "마이페이지 회원정보 수정");
    	return "myPageModification";
    }
}