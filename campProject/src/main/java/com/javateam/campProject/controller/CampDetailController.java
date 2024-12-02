package com.javateam.campProject.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javateam.campProject.domain.CampDTO;
import com.javateam.campProject.domain.CampEntity;
import com.javateam.campProject.domain.CampReservationVO;
import com.javateam.campProject.service.CampReservationService;
import com.javateam.campProject.service.CampingService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CampDetailController {

	@Autowired
	CampingService campingService;

	@Autowired
	CampReservationService campReservationService;

	@GetMapping("/campDetail")
	public String campDetail(@RequestParam("campId") int campId, Model model){

		log.info("[CampDetailController][campDetail]");

		ResponseEntity<CampDTO> responseEntity = null;

		CampDTO campDTO = new CampDTO();
		List<CampDTO> campDTOs = campingService.getCampDetail(campId);
		campDTO = campDTOs.get(0);

		// 서브이미지들 추출
		List<String> subImgs = campDTOs.stream().filter(x -> x.getImgKind().equals("S")).map(x -> x.getImgName()).toList();

		model.addAttribute("camp", campDTO);
		model.addAttribute("subImgs", subImgs);

		return "/camp/campDetail";
	}


	// 캠핑 예약
	@PostMapping("/camp/campReserve")
	public String campReserve(@RequestParam String memberId, @RequestParam String socialUserId,
							  @RequestParam int campCNO, @RequestParam String checkIn, @RequestParam String checkOut ,Model model) {

		log.info("[CampDetailController][campReserve]");

		CampReservationVO campReservationVO = new CampReservationVO();

		// 날짜 변환
		Date checkInDate = Date.valueOf(checkIn);
        Date checkOutDate = Date.valueOf(checkOut);
        log.info("[CampDetailController][checkInDate]: {}", checkInDate);
        log.info("[CampDetailController][checkOutDate]: {}", checkOutDate);

	    // 예약 정보를 설정
        campReservationVO.setCheckIn(checkInDate);
        campReservationVO.setCheckOut(checkOutDate);

        // 예약 처리
        boolean success = campReservationService.insertReservation(campReservationVO, memberId, socialUserId, campCNO, checkInDate, checkOutDate);


        CampEntity campEntity = new CampEntity();
        int campId = campEntity.getId();
        log.info("[CampDetailController][campId]: {}", campId);

        if (success) {
            model.addAttribute("msg", "예약이 완료되었습니다.");
        } else {
            model.addAttribute("msg", "예약에 실패했습니다.");
        }

        return "redirect:/campDetail?campId=" + campId;
    }

}
