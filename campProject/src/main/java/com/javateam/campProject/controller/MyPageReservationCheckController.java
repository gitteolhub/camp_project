package com.javateam.campProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javateam.campProject.domain.CampReservationVO;
import com.javateam.campProject.domain.CampTotReservationVO;
import com.javateam.campProject.domain.CustomUser;
import com.javateam.campProject.domain.MemberVO;
import com.javateam.campProject.domain.SessionUser;
import com.javateam.campProject.service.CampReservationService;
import com.javateam.campProject.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MyPageReservationCheckController {

	@Autowired
	CampReservationService campReservationService;

	@Autowired
	MemberService memberService;

    @GetMapping("/myPageReservationCheck")
    public String myPageReservationCheck(Model model, HttpSession httpSession) {

    	log.info("myPageReservationCheck: ");

    	CampReservationVO campReservationVO = new CampReservationVO();

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

			campReservationVO.setUserName(user.getName());
        	campReservationVO.setUserEmail(memberVO.getEmail());
        	campReservationVO.setUserPhone(memberVO.getPhone());

    	}
    	// 소셜 로그인
    	else {
    		SessionUser sessionUser = (SessionUser)httpSession.getAttribute("socialUser");
        	campReservationVO.setUserName(sessionUser.getName());
        	campReservationVO.setUserEmail(sessionUser.getEmail());
        	campReservationVO.setUserPhone(sessionUser.getMobile());
    	}

    	model.addAttribute("reservationList", campReservationService.selectReservationList(campReservationVO));

        return "myPageReservationCheck"; // myPageReservationCheck.html 파일을 반환
    }

    @GetMapping("/deleteReservation")
    public String deleteReservation(@RequestParam("id") int id, @RequestParam("campId") int campId, Model model) {
    	log.info("deleteReservation: " + id);
    	log.info("deleteReservation: " + campId);

    	if (campReservationService.deleteReservation(id)) {

    		// 전체 캠핑장 예약현황에 반영
    		CampTotReservationVO campTotReservationVO = campReservationService.selectReservationById(campId);
    		int availSite = campTotReservationVO.getAvailSite();
    		log.info("[availSite]: {}", availSite);
    		campReservationService.updateReservation(campId, availSite-1);

    		return "redirect:/myPageReservationCheck";
    	}
    	else {
    		model.addAttribute("errMsg", "예약 취소에 실패하였습니다");
    		model.addAttribute("movePage", "/myPageReservationCheck");
    		return "/error";
    	}
    }
}