package com.javateam.campProject.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;

// 이미지 파일 종류 분석
@Slf4j
public class MediaUtil {
	
	private static Map<String, MediaType> mediaMap;
	
	static {
        mediaMap = new HashMap<>();
        
        mediaMap.put("JPEG", MediaType.IMAGE_JPEG);
        mediaMap.put("JPG", MediaType.IMAGE_JPEG);
        mediaMap.put("GIF", MediaType.IMAGE_GIF);
        mediaMap.put("PNG", MediaType.IMAGE_PNG);
    }
	
	// 미디어 타입 대문자로
	public static MediaType getMediaType(String type) {
		
		log.info("[MediaUtil][getMediaType]");
		
        return mediaMap.get(type.toUpperCase());
    }

	// 주어진 미디어 타입이 이미지 타입인지 확인
	public static boolean containsImageMediaType(String mediaType) {
		
		log.info("[MediaUtil][containsImageMediaType]");
		
        return mediaMap.values().contains(MediaType.valueOf(mediaType));
    }
}
