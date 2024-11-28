package com.javateam.campProject.repository;

import java.util.List;

import com.javateam.campProject.domain.CampVO;

public interface CampDAO {

	List<CampVO> selectCampingName(String campName);

	List<CampVO> selectCate3Name(String cate3);
	
	int countCate3Name(String cate3);
	
	List<CampVO> selectCate3NameByPaging(String searchName, int page, int limit);
	
}
