package com.javateam.campProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javateam.campProject.domain.CampVO;
import com.javateam.campProject.repository.CampDAO;

@Service
public class CampService2Impl implements CampService2 {

	@Autowired
	CampDAO campDAO;
	
	@Override
	public List<CampVO> selectCampingName(String campName) {

		return campDAO.selectCampingName(campName);
	}
}
