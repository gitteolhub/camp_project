package com.javateam.campProject.controller;

import com.javateam.campProject.domain.CampVO;
import com.javateam.campProject.domain.PageListVO;
import com.javateam.campProject.service.CampService;
import com.javateam.campProject.service.CampService2;
import com.javateam.campProject.domain.PageVO;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CampController {

	@Autowired
	private CampService2 campService2;
	
	// 고캠핑API
	private final CampService campService;

	public CampController(CampService campService) {
		this.campService = campService;
	}
	
	@GetMapping(value = "searchCamping", produces = "application/xml; UTF-8")
	public String searchCamping(@RequestParam String keyword) {
		log.info("keyword:" + keyword); // 캠핑장 검색 서비스 호출 return campService.searchCamping(keyword); }
		return keyword;
	}

	// 캠핑 데이터에서 이름 검색
	@GetMapping("/searchCamp")
	public ResponseEntity<PageListVO> searchCamp(@RequestParam("campName") String campName,
			@RequestParam(value="page",defaultValue = "1") int page, @RequestParam(value="limit",defaultValue = "10") int limit) {
		
		log.info("searchCamp:");
		
		// 총 검색된 캠핑 정보 수
		int listCount = campService2.countCampName(campName);
		
		// 페이지 당 레코드들
		List<CampVO> campList = campService2.selectCampNameByPaging(campName, page, limit);
		
		// 총 페이지 수
		int maxPage = PageVO.getMaxPage(listCount, limit);
		int startPage = 1;
		int endPage = maxPage;

   	    if (endPage> maxPage) endPage = maxPage;

   	    PageVO pageVO = new PageVO();
		pageVO.setEndPage(endPage);
		pageVO.setListCount(listCount);
		pageVO.setMaxPage(maxPage);
		pageVO.setCurrPage(page);
		pageVO.setStartPage(startPage);

		pageVO.setPrePage(pageVO.getCurrPage()-1 < 1 ? 1 : pageVO.getCurrPage()-1);
		pageVO.setNextPage(pageVO.getCurrPage()+1 > pageVO.getEndPage() ? pageVO.getEndPage() : pageVO.getCurrPage()+1);
		
		List<CampVO> resultList = new ArrayList<>();
				
		log.info("campList.size:"+campList.size());
		
		for (CampVO campVO : campList) {
			int cNo = campVO.getCNo();
			
			String img = campService2.selectMainImg(cNo);
			
			log.info("img:"+img);
			
			campVO.setMainImg(img);
			resultList.add(campVO);
		}
			
		log.info("resultList.size:"+resultList.size());
		
		PageListVO pageListVO = new PageListVO();
		pageListVO.setCampList(campList);
		pageListVO.setPageVO(pageVO);
		
		return new ResponseEntity<>(pageListVO, HttpStatus.OK);
	}
	
	// 캠핑 데이터에서 카테고리 검색
	@GetMapping("/searchCate3Name")
	public ResponseEntity<PageListVO> searchCate3Name(@RequestParam("cate3") String cate3,
			@RequestParam(value="page",defaultValue = "1") int page, @RequestParam(value="limit",defaultValue = "10") int limit) {
		
		log.info("searchCate3Name:");
		
		// 총 검색된 캠핑 정보 수
		int listCount = campService2.countCate3Name(cate3);
		
		// 페이지 당 레코드들
		List<CampVO> campList = campService2.selectCate3NameByPaging(cate3, page, limit);
		
		// 총 페이지 수
		int maxPage = PageVO.getMaxPage(listCount, limit);
		int startPage = 1;
		int endPage = maxPage;

   	    if (endPage> maxPage) endPage = maxPage;

   	    PageVO pageVO = new PageVO();
		pageVO.setEndPage(endPage);
		pageVO.setListCount(listCount);
		pageVO.setMaxPage(maxPage);
		pageVO.setCurrPage(page);
		pageVO.setStartPage(startPage);

		pageVO.setPrePage(pageVO.getCurrPage()-1 < 1 ? 1 : pageVO.getCurrPage()-1);
		pageVO.setNextPage(pageVO.getCurrPage()+1 > pageVO.getEndPage() ? pageVO.getEndPage() : pageVO.getCurrPage()+1);
		
		List<CampVO> resultList = new ArrayList<>();
				
		log.info("campList.size:"+campList.size());
		
		for (CampVO campVO : campList) {
			int cNo = campVO.getCNo();
			
			String img = campService2.selectMainImg(cNo);
			
			log.info("img:"+img);
			
			campVO.setMainImg(img);
			resultList.add(campVO);
		}
			
		log.info("resultList.size:"+resultList.size());
		
		PageListVO pageListVO = new PageListVO();
		pageListVO.setCampList(campList);
		pageListVO.setPageVO(pageVO);
		
		return new ResponseEntity<>(pageListVO, HttpStatus.OK);
	}
}