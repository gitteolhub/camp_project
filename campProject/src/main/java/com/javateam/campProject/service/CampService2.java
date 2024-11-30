package com.javateam.campProject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.javateam.campProject.domain.CampVO;

@Service
public interface CampService2 {

	// 캠핑장 이름을 조회
	List<CampVO> selectCampingName(String strcamp_name);
	
	// 캠핑장 카테고리(cate3) 조회
	List<CampVO> selectCate3Name(String strcate3);
	
	// 캠핑장별 메인 이미지 조회
	String selectMainImg(int cNo);
	
	// 캠핑장 카테고리 레코드 조회(페이징)
	List<CampVO> selectCate3NameByPaging(String searchName, int page, int limit);
	
	// 캠핑장 카테고리 레코드 총수 조회
	int countCate3Name(String cate3);

	// 캠핑장 이름 레코드 조회(페이징)
	List<CampVO> selectCampNameByPaging(String searchName, int page, int limit);
	
	// 캠핑장 이름 레코드 총수 조회
	int countCampName(String campName);
	
	
}
	
