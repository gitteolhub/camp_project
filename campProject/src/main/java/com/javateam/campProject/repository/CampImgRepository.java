package com.javateam.campProject.repository;

import org.springframework.data.repository.CrudRepository;

import com.javateam.campProject.domain.CampImageVO;

public interface CampImgRepository extends CrudRepository<CampImageVO, Integer>{

	CampImageVO findBycNoAndImgKind(int cNo, String ImgKind);
}