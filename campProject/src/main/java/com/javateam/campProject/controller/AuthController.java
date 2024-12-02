package com.javateam.campProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.javateam.campProject.domain.MemberVO;
import com.javateam.campProject.domain.SessionUser;
import com.javateam.campProject.service.MemberService;
import com.javateam.campProject.service.SocialUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AuthController {

	@Autowired
	MemberService memberService;

	@Autowired
	SocialUserService socialUserService;

	@Autowired
	CaptchaController captchaController;

	// 로그인 틀린 횟수. 3회 실패 시 캡챠 노출
	private int loginErrorCount = 0;
	private final int maxCount = 4;

	// 홈 페이지로 redirection (return문 >> 경로)
	@RequestMapping("/")
	public String root() {

		log.info("[root]");
		return "redirect:/home";
	}

	 // 홈 페이지 메서드 추가
    @GetMapping("/home")
    public String home() {
        log.info("[home]");
        return "home"; // home.html 뷰를 반환
    }

    @GetMapping("/myPage")
    public String myPage(Model model) {
        log.info("[myPage]");

		log.info("로그인 인증됨");
		model.addAttribute("msg", "로그인 되었습니다.");

        return "myPage"; // home.html 뷰를 반환
    }

	// 로그인 페이지를 반환
    @GetMapping("/loginForm")
	public String login( HttpServletRequest request, Model model, HttpSession httpSession) {	//RedirectAttributes redirectAttributes

		log.info("[loginForm]");
		String error = request.getParameter("error")== null ? "없음" : request.getParameter("error");
		String msg   = request.getParameter("msg")  == null ? "없음" : request.getParameter("msg");
		log.info("[loginForm][error]: {}", error);
		log.info("[loginForm][msg]: {}", msg);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.info("login 인증정보: {}", auth);

		// loginErrorCount 세션화
		if(httpSession.getAttribute("loginErrorCount")==null) {
			httpSession.setAttribute("loginErrorCount",0);
		} else {
			loginErrorCount = (Integer)httpSession.getAttribute("loginErrorCount");
			log.info("[loginErrorCount_1]: {}", loginErrorCount);
		}

		String movePath="loginForm";	// 11월21일 수정 (무한루프 패치)

		if(auth.getPrincipal() == null || auth.getPrincipal().toString().equals("anonymousUser")) {

			log.info("로그인 인증 안됨");
			model.addAttribute("error", error);
			model.addAttribute("msg", msg);

			log.info("[loginErrorCount_2: {}]", loginErrorCount);

			if(loginErrorCount >= maxCount) {

				log.info("[if(loginErrorCount >= maxCount)]");
				log.info("[AuthController][msg]: {} ", msg);
				log.info("[AuthController][error]: {} ", error);
				captchaController.captcha(error, msg, model);

			}

		}

		log.info("[AuthController][movePath]: {}", movePath);
		return movePath;
	}

	// 로그아웃 처리 메서드
	@GetMapping("/logoutProc")
	public String logout(Model model, HttpServletRequest request, HttpServletResponse response) {
		log.info("[logout]");

		// 현재 인증 정보
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
		log.info("[loginError][인증 오류]: {}", secuSess.getMessage());

		// 오류 flag 추가
//		redirectAttributes.addAttribute("error", "true");
		model.addAttribute("error", "true");

		// 오류 메서드 추가
//		redirectAttributes.addAttribute("msg", secuSess.getMessage());
		model.addAttribute("msg", secuSess.getMessage());

		// loginErrorCount 세션화
		if(httpSession.getAttribute("loginErrorCount")==null) {
			httpSession.setAttribute("loginErrorCount",0);
		} else {
			loginErrorCount = (Integer)httpSession.getAttribute("loginErrorCount");
			log.info("[loginErrorCount_3]: {}", loginErrorCount);
		}

		loginErrorCount += 1; // 오류 카운트 증가
	    log.info("[loginErrorCount_4]: {}", loginErrorCount);

	    // 세션 변수에 반영
	    httpSession.setAttribute("loginErrorCount", loginErrorCount);

	    // CAPTCHA 활성화 체크
	    if (loginErrorCount >= maxCount) {
	        log.info("[if(loginErrorCount >= maxCount)]");
	        return "redirect:/captcha";

	    }

		return "loginForm";
	}
	// 구글 로그인시 추가정보
	@GetMapping("/socialAddInformation")
	public String socialAddInformation(HttpSession httpSession) {
		SessionUser sessionSocialUser = (SessionUser) httpSession.getAttribute("socialUser");
		log.info("[AuthController][sessionSocialUser]: {}", sessionSocialUser);

		// 회원 정보가 null이거나 "없음"인 경우
	    if (sessionSocialUser != null &&
	        ("없음".equals(sessionSocialUser.getGender()) || sessionSocialUser.getGender() == null) &&
	        ("없음".equals(sessionSocialUser.getBirthday()) || sessionSocialUser.getBirthday() == null) &&
	        ("없음".equals(sessionSocialUser.getMobile()) || sessionSocialUser.getMobile() == null)) {

	    	log.info("[socialAddInformation]");
			return "socialAddInformation";
		} else {
			log.info("[home]");
	        return "redirect:/home"; // 홈으로 이동
		}
	}

	// 비정상 로그인 상황 처리
	@GetMapping("/error")
	public String error() {
		log.info("[error 비정상 로그인 상황 처리]");
		return "redirect:/home";
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

}
