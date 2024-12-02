package com.javateam.campProject.service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
@Slf4j
public class CampService {

    @Value("${gocamping.api.key}")
    private String apiKey; // 고캠핑 API 키

    private final RestTemplate restTemplate;

    public CampService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    
    public String searchCamping(String keyword) {
    	log.info("searchCamping");
        // 고캠핑 API 호출 URL (적절한 API URL로 바꿔주세요)
    	log.info("apikey:"+apiKey);
    	
        // String url = "https://api.gocamping.or.kr/camping/search?keyword=" + keyword + "&apiKey=" + apiKey;
    	// https://apis.data.go.kr/B551011/GoCamping/searchList?serviceKey=TDltxVmk17zJGqE2cnG2pGiaQDNLwztUOXA0Gqh%2FP3mRaWsqZIszbYQ5%2Ft%2FkGXdfs6rCDD5wIfYojJelGCx80w%3D%3D&MobileOS=ETC&MobileApp=AppTest&keyword=%EA%B0%80%ED%8F%89
    	// String url = "https://apis.data.go.kr/B551011/GoCamping/searchList?serviceKey=" + apiKey + "&amp;MobileOS=ETC&amp;MobileApp=AppTest&amp;keyword=" + keyword;
    	// String url ="https://apis.data.go.kr/B551011/GoCamping/searchList?serviceKey=TDltxVmk17zJGqE2cnG2pGiaQDNLwztUOXA0Gqh%2FP3mRaWsqZIszbYQ5%2Ft%2FkGXdfs6rCDD5wIfYojJelGCx80w%3D%3D&MobileOS=ETC&MobileApp=AppTest&keyword=%EA%B0%80%ED%8F%89";
    	String url = "https://apis.data.go.kr/B551011/GoCamping/searchList";
    	url = UriComponentsBuilder.fromHttpUrl(url)
				  .queryParam("serviceKey", apiKey)
				  .queryParam("MobileOS", "ETC")
				  .queryParam("MobileApp", "AppTest")
				  .queryParam("keyword", keyword)
				  .build()
				  .toUriString();
    	
    	url = URLDecoder.decode(url, StandardCharsets.UTF_8);
    	
    	// HTTP header 설정
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/xml; charset=UTF-8");
		// headers.add("Authorization", KAKAO_APIKEY);
    	HttpEntity<String> entity = new HttpEntity<String>(headers);
    	
        // API 호출
        // ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
    	ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    	
    	String resultStr = new String(response.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        
        log.info("response.getBody:"+response.getBody());
        log.info("response Code : "+response.getStatusCode());
        
        // 응답 데이터를 반환
        return resultStr;
    }
    
}
	
