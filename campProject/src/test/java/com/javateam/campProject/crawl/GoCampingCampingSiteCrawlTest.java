package com.javateam.campProject.crawl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.campProject.domain.CampEntity;
import com.javateam.campProject.domain.CampSiteVO;
import com.javateam.campProject.repository.CampRepository;
import com.javateam.campProject.repository.CampSiteRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class GoCampingCampingSiteCrawlTest {

	@Autowired
	CampRepository campRepository;

	@Autowired
	CampSiteRepository campSiteRepository;

	public int getSiteNum(String str) {

		int sum = 0;

		String siteArr[] = null;

		if (str.trim().equals("") == false) {
			siteArr = str.split(" ");
		}

		if (siteArr != null && siteArr.length > 0) {

			for (String s : siteArr) {

				int beginIdx = s.indexOf('(') + 1;

				// 예외적 사항 적용) ex) 일반야영장(30면) 자동차야영장사이트(40면) 덤프스테이션(2기) : "기"로 끝나는 경우
				int endIdx = s.indexOf('면');
				if (endIdx == -1) {
					endIdx = s.indexOf('기');
				} // 추가 코드

				String result = s.substring(beginIdx, endIdx);
				log.info("result : " + result);

				sum += Integer.parseInt(result);

			} // for

		} // if

		log.info("캠핑장 야영공간 갯수 : " + sum);

		return sum;
	}

	@Test
	void testStr() {

		// 일반야영장(30면) 글램핑시설(16면) 카라반(3면) 개인카라반사이트(20면)
		// String str = "일반야영장(30면) 글램핑시설(16면) 카라반(3면) 개인카라반사이트(20면)";
		// String str = "일반야영장(69면)";
		// 추가) 예외적 사항 적용 : ex) 일반야영장(30면)자동차야영장사이트(40면)덤프스테이션(2기) : "기" 로 끝나는 경우
		// String str = "";
		// String str = "자동차야영장사이트(96면) 카라반(15면)";
		String str = "일반야영장(30면) 자동차야영장사이트(40면) 덤프스테이션(2기)";
		String siteArr[] = null;

		if (str.trim().equals("") == false) {
			siteArr = str.split(" ");
		}

		int sum = 0;

		if (siteArr != null && siteArr.length > 0) {

			for (String s : siteArr) {

				int beginIdx = s.indexOf('(') + 1;
				int endIdx = s.indexOf('면');
				if (endIdx == -1) {
					endIdx = s.indexOf('기');
				} // 추가 코드

				String result = s.substring(beginIdx, endIdx);
				log.info("result : " + result);

				sum += Integer.parseInt(result);

			} // for

		} // if

		log.info("캠핑장 야영공간 갯수 : " + sum);
	}

	@Test
	void testFindAll() {

		List<CampEntity> camps = (List<CampEntity>) campRepository.findAll();
		log.info("캠핑장 수 : " + camps.size());

//		for (CampEntity camp : camps) {
//
//			log.info("c_no : " + camp.getCNo());
//		}

	}

	@Test
	void testSave() {

		CampSiteVO campSiteVO = CampSiteVO.builder().cNo(7786).siteContent("글램핑시설(9면)").site(9).petYn("Y").build();

		log.info("campSiteVO : " + campSiteVO);
		campSiteRepository.save(campSiteVO);

	}

	// 주요 공지) camp_site_info 테이블을 구성하면서 camp_info_tbl에
	// 실질적인 c_no(캠핑장 정보 고유 아이디)는 2327개가 아닌 2315개 인 것으로 판정되어서
	// 관광공사에서 집계 및 공급된 정보(CSV)에 약간의 중복 데이터가 있음을 확인할 수 있었음.
	// 가령 select count(*) from (select distinct c_no from camp_info_tbl);
	// SQL 구문을 통해서 확인하면 2315개로 정보 수량이 집계되고 실제로 camp_site_info 과정에서도
	// 2315개로 파악됨.

	// 참고) c_no가 중복된 캠핑장 정보들
	// SELECT C_NO, COUNT(C_NO) FROM CAMP_INFO_TBL INFO
	// GROUP BY  C_NO
	// HAVING COUNT(C_NO) = 2;

	//
	//
	// CAMP_SITE_TBL(캠핑장별 야영공간 현황/갯수/반려동물 동반 가능 여부 등 집계) 레코드 자동 생성(크롤링)
	// getSiteNum 메서드(캠핑장별 야영공간 갯수 파악) 개선 : "~기"로 끝나는 경우도 적용
	// ex) 일반야영장(30면) 자동차야영장사이트(40면) 덤프스테이션(2기)
	@Test
	void testSiteCrawl() {

		List<CampEntity> camps = (List<CampEntity>) campRepository.findAll();

		List<Integer> cNoList = camps.stream().map(x -> x.getCNo()).filter(x -> x != 0).toList();

		List<Integer> notSavedCNos = new ArrayList<>();

		// ex)
		// 주요시설
		// 자동차야영장사이트(38면)

		// 주요시설
		// 일반야영장(37면) 자동차야영장사이트(31면) 글램핑시설(6면) 카라반(3면)

		// 주요시설
		// 일반야영장(69면)

		// 결측치 원인)
		// ex) 일반야영장(30면) 자동차야영장사이트(40면) 덤프스테이션(2기)
		// c_no = 101248의 경우 : "기" 접미사 유입됨 ex) 덤프스테이션(2기)

		// ex)
		// 사이트 크기
		// 4 X 5 : 67개
		//
		// 사이트 크기
		// 5 X 8 : 17개 5 X 9 : 8개5 X 7 : 23개
		//
		loop_cNoList:
		for (int cNo : cNoList) {

			log.info("검색할 C_NO : " + cNo);

			// 개별 캠핑장 소개 페이지
			String url = "https://gocamping.or.kr/bsite/camp/info/read.do?c_no="+cNo;

			try {

				Document doc = Jsoup.connect(url).get();

				// 도로명주소 일치성 확인
				try {

					log.info("site 현황 파악");

					String site = doc.select("table[class='table_t4 camp_etc_tb'] tbody.t_c ul.table_ul05").get(0).text().trim();

					int siteNum = getSiteNum(site); // 메서드 개선 : "~기"로 끝나는 경우도 적용
					// ex) 일반야영장(30면) 자동차야영장사이트(40면) 덤프스테이션(2기)
					log.info("site 현황 : {}, 캠핑장 내 총 site 수 : {}", site, siteNum);

					// ex) 일반야영장(30면) 글램핑시설(16면) 카라반(3면) 개인카라반사이트(20면)
					// "면"이라는 단어에 들어가는 갯수 총계 파악

					// 반려견 동반 여부
					String petYn = "N";

					Elements facilsDetails = doc.select("table[class='table_t4 camp_etc_tb'] tbody.t_c tr");

					facil_loop:
					for (Element facilDetail : facilsDetails) {

						// log.info("text : " + facilDetail.select("th[scope=col]").text());
						// log.info("text2 : " + facilDetail.select("td.etc_type").text());

						if (facilDetail.select("th[scope=col]").text().trim().equals("반려동물 출입") == true) {

							String petYnStr = facilDetail.select("td.etc_type").text();
							petYn = petYnStr.equals("불가능") ? "N" : "Y";

							log.info("반려동물 동반 가능 여부 : " + petYn);

							break facil_loop;
						}

					} // for


					CampSiteVO campSiteVO = CampSiteVO.builder().cNo(cNo).siteContent(site).site(siteNum).petYn(petYn).build();

					log.info("campSiteVO : " + campSiteVO);
					campSiteRepository.save(campSiteVO);

					log.info("저장");

				} catch (Exception e) {

					log.error("저장 안된 C_NO : " + cNo);
					notSavedCNos.add(cNo);
					continue;
				}

			} catch (IOException e) {
				log.error("해당 사이트를 크롤링할 수 없습니다.");
				e.printStackTrace();
			} //

		} // for

		log.info("미저장된 캠핑장 아이디들 현황");
		for (int num : notSavedCNos) {
			log.info("미저장 cNo : " + num);
		}

		// 리스트 현황 파일 저장(NotSavedCnoList.txt)
		try {

			Files.writeString(Paths.get("NotSavedCnoList.txt"), notSavedCNos.toString(), StandardOpenOption.CREATE);

		} catch (IOException e) {
			log.error("저장 오류");
			e.printStackTrace();
		}

	}

} //