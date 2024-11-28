package com.javateam.campProject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.javateam.campProject.domain.CampVO;

@Service
public interface CampService2 {

	// 캠핑장 이름을 조회
	List<CampVO> selectCampingName(String strcamp_name);
	
    
}
	
