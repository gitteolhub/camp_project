package com.javateam.campProject.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.javateam.campProject.service.MemberService;

import jakarta.servlet.http.HttpSession;

import com.javateam.campProject.service.EmailService;
import com.javateam.campProject.domain.MemberVO;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class FindUserIdPwController {

	@Autowired
	MemberService memberService;
	
	@Autowired
	EmailService emailService;

	// 자체 회원 아이디 찾기
	@GetMapping("/findUserId")
	public String findUserId(Model model) {
		model.addAttribute("memberVO", new MemberVO());

		log.info("[FindUserIdPwController][findUserId]");
		return "findUserId";
	}

	@GetMapping("/findUserIdProc")
	public String findUserIdProc(@RequestParam("strName") String strName, @RequestParam("strEmail") String strEmail, Model model, RedirectAttributes redirectAttributes) {
		log.info("[findUserIdProc]: strName={},strEmail={}", strName, strEmail);
		
		try {
			String userId = memberService.findUserIdByNameEmail(strName, strEmail);

			log.info("[FindUserIdPwController][POST_findUserId]");
			
	        if (userId != null) {
	            model.addAttribute("userId", userId); // 아이디를 모델에 추가
	        } else {
	            model.addAttribute("error", "아이디를 찾을 수 없습니다."); // 아이디가 없을 때의 메시지
	        }

	        return "findUserIdResult"; // 아이디 결과 페이지로 이동

		} catch(Exception ex) {
			redirectAttributes.addFlashAttribute("error", ex);

			log.info("[FindUserIdPwController][findUserIdProc] Exception");
			return "redirect:/loginForm";
		}
		
	}
	
	// 자체 회원 비밀번호 찾기
	@GetMapping("/findUserPw")
	public String findUserPw() {
		log.info("[FindUserIdPwController][findUserPw]");
		return "findUserPw";
	}
	
	// 자체 회원 비밀번호 찾기
//	http://localhost:8181/campProject/findUserPwProc?strId=abcd1111
	@GetMapping("/findUserPwProc")
	public String findUserPw(@RequestParam("strId") String strId, Model model, HttpSession httpSession) {
		log.info("[FindUserIdPwController][findUserPwProc]");
		
		// 아이디로 이메일 조회
		String email = memberService.findUserEmailByID(strId);
		log.info("[Post_findUserPw][email]: {}", email);
		
		if(email == null) {
			model.addAttribute("error", "해당 아이디가 존재하지 않습니다.");
			return "findUserPw";
		}
		
		log.info("[findUserPwProc][회원 아아디 존재]");
		// 랜덤 인증 코드 생성
		String verificationCode = emailService.sendRandomCode();
		
		// 이메일 서비스 사용하여 인증 이메일 전송
		emailService.sendSimpleMessage("tgdcom7@naver.com", email, "비밀번호 찾기 인증 코드", "인증 코드: " + verificationCode);

		// 인증코드 저장
		Map<String, String> map = emailService.saveCode(email, verificationCode);
		
		// 인증코드 세션화
		if(httpSession.getAttribute("sess_verificationCode") == null) {
			httpSession.setAttribute("sess_verificationCode", map);
		}
		
		// 이메일 정보
		model.addAttribute("email", email);
		
		return "pwVerificationCode";
	}
	
	// 입력된 코드를 검증하는 post 요청
	@PostMapping("/pwVerificationCode")
	public String pwVerificationCode(@RequestParam("code") String code,   @RequestParam("email") String email,
									 @RequestParam("newPw") String newPw, @RequestParam("confirmPw") String confirmPw, Model model, HttpSession httpSession) {
		log.info("[FindUserIdPwController][pwVerificationCode]");
		
		// 인증 코드 검증
		if (emailService.verityCode(email, code, httpSession)) {
			log.info("[FindUserIdPwController][인증 코드 성공]");
			
			// 비밀번호 확인
			if(!newPw.equals(confirmPw)) {
				model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
				return "redirect:/pwVerificationCode";
			}
			
			// 이메일로 회원 Id 조회
			String userId = memberService.findUserIdByEmail(email);
			log.info("[pwVerificationCode][userId]: {}", userId);
			
			// 비밀번호 업데이트
			memberService.updatePw(userId, newPw);
			log.info("[pwVerificationCode][비밀번호 업데이트 성공]");
			
			return "loginForm";
		} else {
			model.addAttribute("error", "인증 코드를 잘 못 입력하셨습니다.");
			log.info("[FindUserIdPwController][인증 코드 실패]");
			
			return "redirect:/loginForm";
		}
	}

}
