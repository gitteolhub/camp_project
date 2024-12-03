package com.javateam.campProject.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javateam.campProject.domain.CampReservationVO;
import com.javateam.campProject.domain.MemberVO;
import com.javateam.campProject.domain.SocialUser;
import com.javateam.campProject.service.CampReservationService;
import com.javateam.campProject.service.MemberService;
import com.javateam.campProject.service.SocialUserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CampReservationController {

	@Autowired
	CampReservationService campReservationService;

	@Autowired
	MemberService memberService;

	@Autowired
	SocialUserService socialUserService;

	// 캠핑 예약
	@PostMapping("/camp/campReserve")
	public String campReserve(@RequestParam String memberId, @RequestParam String socialUserId,
							  @RequestParam int campId,      @RequestParam int campCNO,
							  @RequestParam String dates, Model model) {
		// @RequestParam String checkIn,  @RequestParam String checkOut,
		log.info("[CampDetailController][campReserve]");
		log.info("[CampDetailController][campReserve][memberId]: {}", memberId);
		log.info("[CampDetailController][campReserve][socialUserId]: {}", socialUserId);
		log.info("[CampDetailController][campReserve][campId]: {}", campId);
		log.info("[CampDetailController][campReserve][campCNO]: {}", campCNO);
		log.info("[CampDetailController][campReserve][dates]: {}", dates); //  2024.12.02 to 2024.12.04

		// 이동할 URL
		String movePage="";

		// 날짜 성분 분리
		String[] datesArr = dates.replaceAll("\\.", "-").trim().split(" to ");

		String checkIn = "";
		String checkOut = "";

		if(datesArr.length==1) {
			checkIn = datesArr[0];
			checkOut = datesArr[0];
		} else {
			checkIn = datesArr[0];
			checkOut = datesArr[1];
		}

		log.info("[CampDetailController][campReserve][checkIn]: {}", checkIn);
		log.info("[CampDetailController][campReserve][checkOut]: {}", checkOut);

		// 날짜 변환
		Date checkInDate  = Date.valueOf(checkIn);
        Date checkOutDate = Date.valueOf(checkOut);
        log.info("[CampDetailController][checkInDate]: {}", checkIn);
        log.info("[CampDetailController][checkOutDate]: {}", checkOut);

		CampReservationVO campReservationVO = new CampReservationVO();

	    // 예약 정보를 설정
        campReservationVO.setCheckIn(checkInDate);
        campReservationVO.setCheckOut(checkOutDate);
        campReservationVO.setCampCNo(campCNO);
        campReservationVO.setCampId(campId);

        if(memberId.equals("") == false) { // 자체 로그인
        	MemberVO memberVO = memberService.selectMemberById(memberId);
        	campReservationVO.setUserName(memberVO.getName());
        	campReservationVO.setUserEmail(memberVO.getEmail());
        	campReservationVO.setUserPhone(memberVO.getPhone());
        } else { // 소셜 로그인
        	String[] arr = socialUserId.split("_");
        	String email = arr[0];
        	String authVendor = arr[1];
        	SocialUser socialUser = socialUserService.selectSocialUser(email, authVendor);

        	campReservationVO.setUserName(socialUser.getName());
        	campReservationVO.setUserEmail(email);
        	campReservationVO.setUserPhone(socialUser.getMobile());
        }

        // 예약 룰(제약조건 3가지)
        // 사유: 세부예약은 개별 캠핑장 시스템의 역할
        // 1. 같은 기간 같은 캠핑장 중복 예약 불가
        // 2. 같은 기간 다른 캠핑장 중복 예약 불가
        // 3. 예약할 캠핑장 최대 사이트 수(예약 가능 캠핑장 수) 와 비교 후 예약 가능 여부 점검

        // 예약 가능 여부(기본값: 예약가능=true)
        boolean isEnableReservation = true;

        // 기존 예약 현황들 조회(리스트)
        List<CampReservationVO> campReservationList = campReservationService.selectReservationList(campReservationVO);
        log.info("[CampDetailController][campReservationList]: {}", campReservationList.size());

        for(CampReservationVO tempCampReservationVO : campReservationList) {

        	// 예약 가능이면 true, 예약 불가(중복)이면 false
            if(campReservationVO.equals(tempCampReservationVO) == false) {	// 1,2번 조건 해당
            	isEnableReservation = false;

            	log.info("[CampDetailController][campReservationVO]: {}", campReservationVO);
            	log.info("[CampDetailController][tempCampReservationVO]: {}", tempCampReservationVO);
            	log.info("[msg]: {}", "중복기간 예약이 있습니다.");
            	break;
            }

        }// for

        log.info("[CampDetailController][isEnableReservation]: {}", isEnableReservation);

        // 3-1 예약할 캠핑장 사이트수 조회
        // 3-2 해당 캠핑장에 예약 가능 사이트를 조회 후 비교
        boolean isEnableAvailSite = false;

        if(isEnableReservation == true) {

        	isEnableAvailSite = campReservationService.isEnableReservation(campReservationVO);
        }

        log.info("[CampDetailController][isEnableAvailSite(해당 캠핑장에 예약 가능 사이트를 조회 후 비교 후)]: {}", isEnableAvailSite);

        if(isEnableReservation == true && isEnableAvailSite == true) {

        	// 예약 처리
        	boolean success = campReservationService.insertReservation(campReservationVO, memberId, socialUserId, campCNO, checkInDate, checkOutDate);
        	if (success) {
                model.addAttribute("errMsg", "예약이 완료되었습니다.");
            } else {
            	model.addAttribute("errMsg", "시스템 문제로 예약 실패했습니다.");
            }

        } else { // 에약 실패

        	model.addAttribute("errMsg", "중복기간 예약이 있습니다.");
        }

        movePage = "/campDetail?campId=" + campId;
    	model.addAttribute("movePage", movePage);

        return "/error";
    }
}
