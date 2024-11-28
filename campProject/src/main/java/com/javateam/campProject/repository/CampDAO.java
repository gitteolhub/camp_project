package com.javateam.campProject.repository;

import java.util.List;

import com.javateam.campProject.domain.CampVO;

public interface CampDAO {

	List<CampVO> selectCampingName(String strcamp_name);
	
}
