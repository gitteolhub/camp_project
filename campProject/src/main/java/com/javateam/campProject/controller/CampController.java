package com.javateam.campProject.controller;

import com.javateam.campProject.service.CampService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CampController {

    private final CampService campService;

    public CampController(CampService campService) {
        this.campService = campService;
    }
    
      
	@GetMapping(value="searchCamping", produces="application/xml; UTF-8")
    public String searchCamping(@RequestParam String keyword) {
    	log.info("keyword:"+ keyword);
        // 캠핑장 검색 서비스 호출
        return campService.searchCamping(keyword);
    }
}
