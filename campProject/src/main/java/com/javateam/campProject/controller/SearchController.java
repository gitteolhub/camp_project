package com.javateam.campProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
        // 카카오 API를 호출하고 결과를 가져와서 모델에 추가 (서버에서 API 호출)
        // 예시로 카카오 API 호출은 Java SDK를 사용하거나, HTTP 요청을 통해 가능합니다.
        model.addAttribute("keyword", keyword);
        return "search";  // HTML 페이지로 결과 전달
    }
}
