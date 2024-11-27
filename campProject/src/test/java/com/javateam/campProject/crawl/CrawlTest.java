package com.javateam.campProject.crawl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.campProject.domain.CampCrawlDTO;  // 무슨 파일?
import com.javateam.campProject.domain.CampImgVO;
import com.javateam.campProject.repository.CampImgRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class CrawlTest {


	@Autowired
	CampImgRepository campImgRepository;


	@Value("${campImg.storePath}")
	String savePath;


	public void saveCampMainImg(int cNo) {


		String url = "https://gocamping.or.kr/bsite/camp/info/read.do?c_no="+cNo;


		CampImgVO campimgVO = new CampImgVO();
		campimgVO.setCNo(cNo);
		campimgVO.setImgKind("M");


		try {


			Document doc = Jsoup.connect(url).get();
			String mainImgPath = doc.select("div[class='camp_info_box'] div[class='img_b'] img").attr("src");


			// 메인 이미지 다운로드
			// <img src="/upload/camp/4/thumb/thumb_720_4548WQ5JCsRSkbHrBAaZylrQ.jpg" alt="(주)디노담양힐링파크 지점 메인 이미지">
			String mainImgUrl = "https://gocamping.or.kr" + mainImgPath;
			int delimeterIdx = mainImgPath.lastIndexOf('/');
			String mainImgFilename = mainImgPath.substring(delimeterIdx + 1);


			log.info("이미지 저장(savePath) : " + savePath);


			try {


				InputStream in = new URL(mainImgUrl).openStream();
				Files.copy(in, Paths.get(savePath + mainImgFilename), StandardCopyOption.REPLACE_EXISTING);


				campimgVO.setImgName(mainImgFilename);


			} catch (Exception e) {
				log.error("그림 파일 저장 에러 : " + e);
			} //


			// DB Table(camp_img_tbl) 저장
			campImgRepository.save(campimgVO);


		} catch (IOException e) {
			log.error("해당 사이트를 크롤링할 수 없습니다.");
			e.printStackTrace();
		} //


	}


	public void saveCampSubImg(int cNo) {


		String url = "https://gocamping.or.kr/bsite/camp/info/read.do?c_no="+cNo;


		CampImgVO campimgVO = null;


		try {


			Document doc = Jsoup.connect(url).get();
			Elements imgs = doc.select("div[class='camp_intro'] li[class='col_03 img_box'] img");


			for (Element img : imgs) {


				campimgVO = new CampImgVO();
				campimgVO.setCNo(cNo);
				campimgVO.setImgKind("S");


				String imgPath = img.attr("src");


				// 메인 이미지 다운로드
				// <img src="/upload/camp/4/thumb/thumb_384_8460x14p2wteha7SjLLANUhu.jpg" class="imgFit" alt="캠핑장소개 이미지">
				String imgUrl = "https://gocamping.or.kr" + imgPath;
				int delimeterIdx = imgPath.lastIndexOf('/');
				String mainImgFilename = imgPath.substring(delimeterIdx + 1);


				log.info("갤러리 이미지 저장(savePath) : " + savePath);


				try {


					InputStream in = new URL(imgUrl).openStream();
					Files.copy(in, Paths.get(savePath + mainImgFilename), StandardCopyOption.REPLACE_EXISTING);


					campimgVO.setImgName(mainImgFilename);


				} catch (Exception e) {
					log.error("그림 파일 저장 에러 : " + e);
				} //


				// DB Table(camp_img_tbl) 저장
				campImgRepository.save(campimgVO);


			} // for


		} catch (IOException e) {
			log.error("해당 사이트를 크롤링할 수 없습니다.");
			e.printStackTrace();
		} //


	}


	// C_NO 리스트를 저장하는 파일 생성
	@Test
	public void testPagingSearchAndSaveCNo() {


		Set<Integer> cNoSet = new TreeSet<>();


		// 총 페이지 수 : 페이지당 50개씩 총 83페이지
		for (int i=1; i<=83; i++) {


			String url = "https://gocamping.or.kr/bsite/camp/info/list.do?pageUnit=50&searchKrwd=&listOrdrTrget=c_rdcnt&pageIndex=" + i;


			try {


				 Document doc = Jsoup.connect(url).get();


				 Elements campPanels = doc.select("div.camp_search_list div[class='c_list update']");


				 for (Element campPanel : campPanels) {


					 CampCrawlDTO campCrawlDTO = new CampCrawlDTO();


					 // /bsite/camp/info/read.do?c_no=1776&viewType=read01&listOrdrTrget=c_rdcnt


					 // cNo(아이디) 추출
					 String campURL = campPanel.select("a").attr("href");
					 int cNoIdx = campURL.split("&")[0].indexOf("c_no=") + 5;
					 int cNo = Integer.parseInt(campURL.split("&")[0].substring(cNoIdx));
					 log.info("cNo : " + cNo);


					 cNoSet.add(cNo);
				 } //


			} catch (IOException e) {
				log.error("해당 사이트를 크롤링할 수 없습니다.");
				e.printStackTrace();
			} //


		} // for


		try {


			Files.writeString(Paths.get("cNoSet.txt"), cNoSet.toString(), StandardOpenOption.CREATE);


		} catch (IOException e) {
			log.error("저장 오류");
			e.printStackTrace();
		}


	} //


	// 개별 캠핑장 이미지 저장(메인/서브 이미지) (DB + 저장소)
	@Test
	public void test() {


		this.saveCampMainImg(4);
		this.saveCampSubImg(4);
	} //
/////////////////////////////////////////////////////////////////////////////////////////////
	
	// 위쪽 코드는 코드 생성 과정이므로 실제 사용x
	// 전체 캠핑장 이미지 저장(DB + 저장소)
	@Test
	public void testFullCrawl() {

		// 고캠핑 사이트 캠핑장 고유번호(c_no) 리스트 조회
		List<Integer> cNoList = new ArrayList<>();

		try {
			String result = Files.readString(Paths.get("cNoSet.txt"));
			String resultArr[] = result.substring(1, result.length()-1).split(",");
			List<String> tempList = new ArrayList<>();
			tempList.addAll(Arrays.asList(resultArr));


			cNoList = tempList.stream().map(x -> Integer.parseInt(x.trim())).toList();


		} catch (IOException e) {
			log.error("읽기 오류");
			e.printStackTrace();
		}

		log.info("고캠핑 캠핑장 레코드 수 : " + cNoList.size()); // 4130 개소

		// subList 메서드 사용하여 분할 크롤링 가능
		for (int cNo : cNoList.subList(100, cNoList.size())) {
			saveCampMainImg(cNo);
			saveCampSubImg(cNo);
		} // for

	} //

}