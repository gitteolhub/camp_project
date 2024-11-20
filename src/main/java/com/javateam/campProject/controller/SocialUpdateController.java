package com.javateam.campProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.javateam.campProject.domain.SessionUser;
import com.javateam.campProject.domain.SocialUser;
import com.javateam.campProject.service.SocialUserService;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/social")
@Slf4j
public class SocialUpdateController {

	@Autowired
	SocialUserService SocialUserService;

	@PostMapping("updateGoogle")
	public String updateGoogle(@ModelAttribute SocialUser socialUser, HttpSession httpSession, RedirectAttributes redirectAttributes) {
		log.info("[updateGoogle]: {}", socialUser);

		String msg      = "";
		String movePath = "";

		socialUser.setAuthVendor("google"); // AuthVendor열에 "google"
		socialUser.setMemberType("회원"); // MemberType열에 "회원"

		boolean blRetVal = SocialUserService.updateSocialGoogleUser(socialUser);

		if(blRetVal == true) {

			msg      = "가입 되었습니다.";
			movePath = "redirect:/home";

			// 기존 session 정보 갱신(성별, 생일, 전화번호)
			SessionUser sessionSocialUser = (SessionUser)httpSession.getAttribute("socialUser");
			log.info("[sessionSocialUser]: {}", sessionSocialUser);

			sessionSocialUser.setGender(socialUser.getGender());
			sessionSocialUser.setBirthday(socialUser.getBirthday());
			sessionSocialUser.setMobile(socialUser.getMobile());
			httpSession.setAttribute("socialUser", sessionSocialUser);

		} else {

			msg      = "가입 실패했습니다.";
			movePath = "redirect:/socialAddInformation";
		}
		log.info("[updateGoogle][result] : {}", msg);
		redirectAttributes.addAttribute("msg", msg);


		return movePath;
	}

}
