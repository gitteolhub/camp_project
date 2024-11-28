package com.javateam.campProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.campProject.domain.CampVO;
import com.javateam.campProject.repository.CampDAO;
import com.javateam.campProject.repository.CampImgRepository;

@Service
public class CampService2Impl implements CampService2 {

	@Autowired
	CampDAO campDAO;
	
	@Autowired
	CampImgRepository campImgRepository;
	
	@Transactional(readOnly = true)
	@Override
	public List<CampVO> selectCampingName(String campName) {

		return campDAO.selectCampingName(campName);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<CampVO> selectCate3Name(String cate3) {

		return campDAO.selectCate3Name(cate3);
	}
	
	@Transactional(readOnly = true)
	@Override
	public String selectMainImg(int cNo) {

		return campImgRepository.findBycNoAndImgKind(cNo, "M").getImgName();
	}

	@Transactional(readOnly = true)
	@Override
	public List<CampVO> selectCate3NameByPaging(String searchName, int page, int limit) {

		return campDAO.selectCate3NameByPaging(searchName, page, limit);
	}
	
	@Transactional(readOnly = true)
	@Override
	public int countCate3Name(String cate3) {

		return campDAO.countCate3Name(cate3);
	}
	
	
}
