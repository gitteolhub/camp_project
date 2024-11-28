package com.javateam.campProject.controller;

import com.javateam.campProject.domain.CampVO;
import com.javateam.campProject.service.CampService;
import com.javateam.campProject.service.CampService2;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CampController {

	    @Autowired
	    private CampService2 campService2;

	    @GetMapping("/searchCamp")
	    public ResponseEntity<List<CampVO>> searchCampgrounds(@RequestParam("campName") String campName) {
	    	log.info("searchCamp:");
	    	return new ResponseEntity<>(campService2.selectCampingName(campName),HttpStatus.OK);  
	    }
	
	// 고캠핑API
	
	 private final CampService campService;
	 
	 public CampController(CampService campService) { this.campService =
	 campService; }
	 
	 @GetMapping(value="searchCamping", produces="application/xml; UTF-8") public
	 String searchCamping(@RequestParam String keyword) { log.info("keyword:"+
	 keyword); // 캠핑장 검색 서비스 호출 return campService.searchCamping(keyword); }
	return keyword;
	 }
}