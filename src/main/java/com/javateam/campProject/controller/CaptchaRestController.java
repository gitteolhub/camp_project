package com.javateam.campProject.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javateam.campProject.service.ApiCaptchaImageService;
import com.javateam.campProject.service.ApiCaptchaNkeyService;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CaptchaRestController {

	@Value("${naver.captcha.clientId}")
	public String clientId; // 애플리케이션 클라이언트 아이디값

	@Value("${naver.captcha.clientSecret}")
	public String clientSecret; // 애플리케이션 클라이언트 시크릿값

	@Autowired
	public ApiCaptchaImageService apiCaptchaImageService;

	@Autowired
	public ApiCaptchaNkeyService apiCaptchaNKeyService;

	@Autowired
	public ServletContext servletContext;

	// captcha 이미지 갱신(refresh)
	@GetMapping("refreshImage")
	public ResponseEntity<String> refreshImage() {

		log.info("[refreshImage][clientId]: {}", clientId);
		log.info("[refreshImage][clientSecret]: {}", clientSecret);

		String responseBody = "";

		Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        String code    = "0"; // 키 발급시 0,  캡차 이미지 비교시 1로 세팅
        String apiURL  = "https://openapi.naver.com/v1/captcha/nkey?code=" + code;
        String keyJson = apiCaptchaNKeyService.get(apiURL, requestHeaders);

        ObjectMapper objectMapper = new ObjectMapper();
        String key = "";

        try {
        	key = objectMapper.readValue(keyJson, Map.class).get("key").toString();
        	log.info("[refreshImage][key(result)]: {}", key);

        } catch (IOException ex) {
        	log.error("[refreshImage][JSON parsing error]");
        	ex.printStackTrace();

        }

        apiURL = "https://openapi.naver.com/v1/captcha/ncaptcha.bin?key=" + key;
        String filenameOrMsg = apiCaptchaImageService.get(apiURL,requestHeaders);

        log.info("[filenameOrMsg(메시지)]: {}", filenameOrMsg);

        Map<String, String> map = new HashMap<>();
    	map.put("captchaImage", filenameOrMsg);
		map.put("key", key);

		try {
			responseBody = objectMapper.writeValueAsString(map);

		} catch (IOException ex) {
			log.error("[JSON 생성 에러]");
			ex.printStackTrace();
		}

		log.info("[refreshImage][responseBody]: {}", responseBody);

		return new ResponseEntity<String>(responseBody, HttpStatus.OK);
	}

	// captcha 이미지 비교 점검(check)
	@GetMapping("checkCaptcha")
	public ResponseEntity<String> rest(@RequestParam("captchaVal") String captchaVal,
									   @RequestParam("key") String key) {

		log.info("[checkCaptcha]");

		log.info("[checkCaptcha][clientId]: {}", clientId);
		log.info("[checkCaptcha][clientSecret]: {}", clientSecret);

		log.info("[checkCaptcha][key]: {}", key);
		log.info("[checkCaptcha][captchaVal]: {}", captchaVal);

		// 키 발급시 0, 캡차 이미지 비교시 1로 세팅
		String code = "1";
		String apiURL = "https://openapi.naver.com/v1/captcha/nkey?code=" + code
  			  + "&key=" + key + "&value=" + captchaVal;

  		Map<String, String> requestHeaders = new HashMap<>();
  		requestHeaders.put("X-Naver-Client-Id", clientId);
  		requestHeaders.put("X-Naver-Client-Secret", clientSecret);

  		String responseBody = apiCaptchaNKeyService.get(apiURL, requestHeaders);

  		log.info("[checkCaptcha][responseBody]: {}", responseBody);

  		return new ResponseEntity<String>(responseBody, HttpStatus.OK);
	}

}
