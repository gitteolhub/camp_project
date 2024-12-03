package com.javateam.campProject.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.campProject.domain.CampEntity;
import com.javateam.campProject.domain.CampSiteVO;
import com.javateam.campProject.domain.CampTotReservationVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class CampTotReservationRepoTest {

	@Autowired
	CampTotReservationRepository campTotReservationRepository;

	@Autowired
	CampSiteRepository campSiteRepository;

	@Autowired
	CampRepository campRepository;

	// 전체 캠핑장 예약현황 테이블(누적현황만 집계) 초기화
	// 주의) 사이트 현황이 없는 경우 기정값을 일괄적으로 10개로 할당
	@Test
	void test() {

		List<CampEntity> camps = (List<CampEntity>)campRepository.findAll();
		List<Integer> campIds = camps.stream().map(x -> x.getId()).toList();

		log.info("campIds : " + campIds.size());

		CampTotReservationVO campTotReservationVO = null;

		for (CampEntity camp : camps) {

			int id = camp.getId();
			int cNo = camp.getCNo();

			int site = 0;
			if (campSiteRepository.findById(cNo).isEmpty() == true) {
				site = 10;
			} else {

				site = campSiteRepository.findById(cNo).get().getSite();

				if (site == 0) {
					site = 10;
				}

			}

			campTotReservationVO = new CampTotReservationVO();

			campTotReservationVO.setCampId(id);
			campTotReservationVO.setSite(site);
			campTotReservationVO.setAvailSite(0);

			campTotReservationRepository.save(campTotReservationVO);
		}

	} //

}

