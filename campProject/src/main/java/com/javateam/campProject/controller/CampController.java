package com.javateam.campProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CampController {

	@GetMapping("/search")
    public String search() {
        log.info("[search]");
        return "search";
    }

	@GetMapping("/searchRecomCamp")
	public String searchRecomCamp() {

		return "searchRecomCamp";
	}
}
