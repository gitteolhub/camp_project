package com.javateam.campProject.service;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.campProject.domain.CampReservationVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class DateDiffTest {

	@Autowired
	CampReservationService campReservationService;

	@Test
	void test() {
		String strDate1 = "2024-12-02";
		Date date1 = Date.valueOf(strDate1);

		String strDate2 = "2024-12-03";
		Date date2 = Date.valueOf(strDate2);
		log.info("[DateDiffTest][date1]: {}", date1.getTime());
		log.info("[DateDiffTest][date2]: {}", date2.getTime());

		assertTrue(date1.getTime() < date2.getTime());

	}

	@Test
	void test2() {
		// 24.12.02 ~ 24.12.04
		// 24.12.03 ~ 24.12.05
		// 시작일1 ~ 종료일1
		// 시작일2 ~ 종료일2

		// 날짜가 겹치지 않는 상황
		// 종료일2 < 시작일1 또는
		// 종료일1 < 시작일2

		Date beginDate1 = Date.valueOf("2024-12-02");
		Date endDate1 = Date.valueOf("2024-12-04");

		Date beginDate2 = Date.valueOf("2024-12-05");
		Date endDate2 = Date.valueOf("2024-12-07");

//		assertTrue(endDate2.getTime() < beginDate1.getTime());
		assertTrue(endDate1.getTime() < beginDate2.getTime());
	}

	// 예약 가능 테스트
	@Test
	void test3() {
		CampReservationVO campReservationVO1 = new CampReservationVO();
		CampReservationVO campReservationVO2 = new CampReservationVO();
		campReservationVO1.setUserName("홍길동");
		campReservationVO2.setUserName("홍길동");

		campReservationVO1.setUserEmail("abcd1111@abcd.com");
		campReservationVO2.setUserEmail("abcd1111@abcd.com");

		campReservationVO1.setUserPhone("010-1111-1111");
		campReservationVO2.setUserPhone("010-1111-1111");

		campReservationVO1.setCheckIn(Date.valueOf("2024-12-02"));
		campReservationVO1.setCheckOut(Date.valueOf("2024-12-04"));

		campReservationVO2.setCheckIn(Date.valueOf("2024-12-05"));
		campReservationVO2.setCheckOut(Date.valueOf("2024-12-07"));

		assertTrue(campReservationVO1.equals(campReservationVO2));	// 예약 가능

	}

	// 예약 불가(중복) 테스트
	@Test
	void test4() {
		CampReservationVO campReservationVO1 = new CampReservationVO();
		CampReservationVO campReservationVO2 = new CampReservationVO();
		campReservationVO1.setUserName("홍길동");
		campReservationVO2.setUserName("홍길동");

		campReservationVO1.setUserEmail("abcd1111@abcd.com");
		campReservationVO2.setUserEmail("abcd1111@abcd.com");

		campReservationVO1.setUserPhone("010-1111-1111");
		campReservationVO2.setUserPhone("010-1111-1111");

//		campReservationVO1.setCheckIn(Date.valueOf("2024-12-02"));
//		campReservationVO1.setCheckOut(Date.valueOf("2024-12-05"));

		// ex1) 12월03일~04일
//		campReservationVO2.setCheckIn(Date.valueOf("2024-12-03"));
//		campReservationVO2.setCheckOut(Date.valueOf("2024-12-04"));

		// ex2) 12월01일~03일
//		campReservationVO2.setCheckIn(Date.valueOf("2024-12-01"));
//		campReservationVO2.setCheckOut(Date.valueOf("2024-12-03"));

		// ex3) 12월04일~07일
//		campReservationVO2.setCheckIn(Date.valueOf("2024-12-04"));
//		campReservationVO2.setCheckOut(Date.valueOf("2024-12-07"));

		// ex4) 12월01일~07일
//		campReservationVO2.setCheckIn(Date.valueOf("2024-12-01"));
//		campReservationVO2.setCheckOut(Date.valueOf("2024-12-07"));

		// ex5) 12월06일~07일 (중복x)
//		campReservationVO2.setCheckIn(Date.valueOf("2024-12-06"));
//		campReservationVO2.setCheckOut(Date.valueOf("2024-12-07"));

		campReservationVO1.setCheckIn(Date.valueOf("2024-12-16"));
		campReservationVO1.setCheckOut(Date.valueOf("2024-12-17"));

		// ex6) 12월01일~01일 (중복x)
		campReservationVO2.setCheckIn(Date.valueOf("2024-12-02"));
		campReservationVO2.setCheckOut(Date.valueOf("2024-12-03"));

		// true: 예약 가능, false: 예약 불가(중복)
		assertFalse(campReservationVO1.equals(campReservationVO2));

	}

	@Test
	void test5() {

		CampReservationVO campReservationVO = new CampReservationVO();
		boolean isEnableReservation = true;

	    // 예약 정보를 설정
		campReservationVO.setUserName("홍길동");
		campReservationVO.setUserPhone("010-1111-1111");
		campReservationVO.setUserEmail("tgdcom7@naver.com");
        campReservationVO.setCheckIn(Date.valueOf("2024-12-02"));
        campReservationVO.setCheckOut(Date.valueOf("2024-12-17"));
        campReservationVO.setCampCNo(7991);
        campReservationVO.setCampId(7);

		// 기존 예약 현황들 조회(리스트)
        List<CampReservationVO> campReservationList = campReservationService.selectReservationList(campReservationVO);
        log.info("[CampDetailController][campReservationList]: {}", campReservationList.size());

        for(CampReservationVO tempCampReservationVO : campReservationList) {

        	// 예약 가능이면 true, 예약 불가(중복)이면 false
            if(campReservationVO.equals(tempCampReservationVO)==false) {	// 1,2번 조건 해당
            	isEnableReservation = false;

            	log.info("[CampDetailController][campReservationVO]: {}", campReservationVO);
            	log.info("[CampDetailController][tempCampReservationVO]: {}", tempCampReservationVO);
            	log.info("[msg]: {}", "중복기간 예약이 있습니다.");
            	break;
            }

        }// for

        log.info("[isEnableReservation]: {}", isEnableReservation);
        assertTrue(isEnableReservation);

	}

	@Test
	void test6() {
		CampReservationVO campReservationVO = new CampReservationVO();

	    // 예약 정보를 설정
		campReservationVO.setUserName("홍길동");
		campReservationVO.setUserPhone("010-1111-1111");
		campReservationVO.setUserEmail("tgdcom7@naver.com");
        campReservationVO.setCheckIn(Date.valueOf("2024-12-16"));
        campReservationVO.setCheckOut(Date.valueOf("2024-12-17"));
        campReservationVO.setCampCNo(6942);
        campReservationVO.setCampId(4);

		boolean isEnableAvailSite = campReservationService.isEnableReservation(campReservationVO);
		assertTrue(isEnableAvailSite);
	}
}
