package com.javateam.campProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javateam.campProject.domain.CampDTO;
import com.javateam.campProject.service.CampingService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CampDetailController {

	@Autowired
	CampingService campingService;

	@GetMapping("/campDetail")
	public String campDetail(@RequestParam("campId") int campId, Model model){

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

}
