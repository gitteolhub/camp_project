package com.javateam.campProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javateam.campProject.domain.UserRequestVO;
import com.javateam.campProject.domain.UserResultVO;
import com.javateam.campProject.service.CampRecomService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class RecomCampRestController {

	@Autowired
	CampRecomService campRecomSvc;

	@GetMapping("/recomCamp")
	public String recomCamp() {

		return "recomCamp";
	}

	@PostMapping("/recomCampProc")
	@ResponseBody
	public ResponseEntity<Object> recomCampProc(@ModelAttribute UserRequestVO userRequestVO) {

		log.info("UserRequestVO : {}", userRequestVO);

		// 질의(Query) 판정
		List<UserResultVO> campList = null;

		// 응답(response) 정보
		ResponseEntity<Object> response = null;
		try {

			campList = campRecomSvc.predictCamp(userRequestVO).stream()
					.sorted((o1, o2) -> o2.getSatisfaction() - o1.getSatisfaction()) // 만족도 순으로 내림차순 정렬(rank-Top)
					.limit(10).toList();

			if (campList.isEmpty() == true) {

				log.info("해당 정보가 없습니다.");
				response = new ResponseEntity<>("해당 정보가 존재하지 않습니다.", HttpStatus.NO_CONTENT);

			} else { // 검색 결과 있음

				response = new ResponseEntity<>(campList, HttpStatus.OK);

			} // 리스트 확보 여부

		} catch (Exception e) {
			log.error("DB 에러");
			response = new ResponseEntity<>("서버 응답에 문제가 있습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		log.info("response : " + response.getStatusCode());

		return response;
	}
}
