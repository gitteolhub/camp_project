package com.javateam.campProject.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.campProject.domain.CampDTO;
import com.javateam.campProject.repository.CampDetailRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class CampRepoTest {

	@Autowired
	CampingService campingService;

	@Test
	void test2() {
		int size = campingService.loadCampData().size();
		log.info("[CampRepoTest][size]: {}", size);
	}

	@Test
	void test() {
		CampDTO resultDTO = new CampDTO();

		List<CampDTO> list = campingService.getCampDetail(1);
//		for(CampDTO campDTO : list) {
//			log.info("[CampRepoTest][campDTO]: " + campDTO);
//		}

		resultDTO = list.get(1);
//		log.info("[CampRepoTest][resultDTO]: {}", resultDTO);

		// 서브이미지들 추출
		List<String> subImgs = list.stream().filter(x -> x.getImgKind().equals("S")).map(x -> x.getImgName()).toList();
		for(String image : subImgs) {
			log.info("[CampRepoTest][image]: " + image);
		}
	}

}
