package com.javateam.campProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.javateam.campProject.domain.CustomUser;
import com.javateam.campProject.domain.MemberUpdateDTO;
import com.javateam.campProject.domain.MemberVO;
import com.javateam.campProject.domain.SessionUser;
import com.javateam.campProject.domain.SocialUser;
import com.javateam.campProject.service.MemberService;
import com.javateam.campProject.service.SocialUserService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MyPageModificationController {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	SocialUserService socialUserService;

    @GetMapping("/myPageModification")
    public String myPageModification(Model model, HttpSession httpSession) {  // Model 인자를 이용해 페이지 타이틀 설정
    	log.info("회원정보 수정");
    	
    	String movePath="";
    	
    	//자체, 소셜 로그인 구분
    	if (httpSession.getAttribute("socialUser")==null) {  // 자체 로그인
    		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		log.info("[auth: {}]", auth);
    		
    		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			CustomUser user = null;
			
			if (principal instanceof CustomUser) {
				user = ((CustomUser)principal);
			}
			
			MemberVO memberVO = memberService.selectMemberById(user.getUsername());
			
			if(memberVO == null) {
				// 회원정보가 없는 경우, 에러 처리
				model.addAttribute("errorMsg", "회원 정보가 존재하지 않습니다.");
				return "/error/error";

			} else {

				MemberUpdateDTO memberUpdateDTO = new MemberUpdateDTO(memberVO);
				model.addAttribute("memberUpdateDTO", memberUpdateDTO);
				
				movePath = "myPageModification";
			}
			
    	}
    	// 소셜 로그인
    	else {
    		SessionUser sessionUser = (SessionUser)httpSession.getAttribute("socialUser");
    		SocialUser socialUser = socialUserService.selectSocialUser(sessionUser.getEmail(), sessionUser.getAuthVendor());
    		model.addAttribute("socialUser", socialUser);
    		
    		movePath = "myPageSocialModification";
    	}

    	model.addAttribute("headerName", "마이페이지 회원정보 수정");  // [[${headerName}]] 부분에 들어갈 페이지 타이틀
    	
        return movePath; // myPageModification.html 파일을 반환
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