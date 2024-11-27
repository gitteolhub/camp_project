package com.javateam.campProject.crawl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.campProject.repository.CampRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class DeleteUselessCampListTest {

	@Autowired
	CampRepository campRepository;

	// 최종 camp_info_tbl 레코드수 : 2313
	@Test
	void testDelete() {

//		삭제할 캠핑장 ID(고캠핑에 정보가 없거나 C_NO 중복인 경우) : 576, 577, 2045, 1219, 922, 1226, 565, 1232, 1233, 849, 1474, 2192, 1311
//
//		576, 577 => 8109 (C_NO) : 캠핑장 이름은 동일하나 주소가 다름 : 강원 화천군 사내면 돈내미길 183-39 ==> 둘다 삭제
//
//		1055 : 이름 변경됨 : 남양주 별빛 캠핑장 => 7159 (C_NO)
//		1311 : 스테리움 => 100173 (C_NO)

		 int []deleteCampArr = {576, 577, 2045, 1219, 922, 1226, 565, 1232, 1233, 849, 1474, 2192, 1311, 1055};

		 for (int id : deleteCampArr) {
			 campRepository.deleteById(id);
		 } // for

	}

}
