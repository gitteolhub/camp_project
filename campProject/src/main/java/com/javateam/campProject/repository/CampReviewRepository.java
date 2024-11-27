package com.javateam.campProject.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.javateam.campProject.domain.CampReviewVO;

public interface CampReviewRepository extends CrudRepository<CampReviewVO, String> {

	List<CampReviewVO> findAllByCampName(String campName);
}