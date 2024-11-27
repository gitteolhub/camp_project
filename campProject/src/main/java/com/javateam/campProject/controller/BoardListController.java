package com.javateam.campProject.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javateam.campProject.domain.BoardVO;
import com.javateam.campProject.domain.PageVO;
import com.javateam.campProject.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("board")
@Slf4j
public class BoardListController {
	
	@Autowired
	BoardService boardService;
	
	@GetMapping("inquiryBoardList")
	public String inquiryBoardList (@RequestParam(value="currentPage", defaultValue="1") int currentPage,
			   						@RequestParam(value="intLimit", defaultValue="10") int intLimit,
			   						Model model) {
		
		log.info("[BoardListController][inquiryBoardList]");	// 게시글 목록
		List<BoardVO> boardList = new ArrayList<>();
		
		// 총 게시글 수 (댓글들을 제외한)
		int listCount = boardService.selectBoardsCountWithoutReplies();
		// 댓글들 제외
		boardList = boardService.selectBoardsByPagingWithoutReplies(currentPage, intLimit);

		// 총 페이지 수
		int maxPage = PageVO.getMaxPage(listCount, intLimit);
		// 현재 페이지에 보여줄 시작 페이지 수 (1, 11, 21,...)
		int startPage = PageVO.getStartPage(currentPage, intLimit);
		// 현재 페이지에 보여줄 마지막 페이지 수(10, 20, 30, ...)
   	    int endPage = startPage + 10;
   	    
   	    if (endPage> maxPage) endPage = maxPage;
   	    
   	    PageVO pageVO = new PageVO();
		pageVO.setEndPage(endPage);
		pageVO.setListCount(listCount);
		pageVO.setMaxPage(maxPage);
		pageVO.setCurrentPage(currentPage);
		pageVO.setStartPage(startPage);
		
		pageVO.setPreviousPage(pageVO.getCurrentPage()-1 < 1 ? 1 : pageVO.getCurrentPage()-1);
		pageVO.setNextPage(pageVO.getCurrentPage()+1 > pageVO.getEndPage() ? pageVO.getEndPage() : pageVO.getCurrentPage()+1);

		model.addAttribute("pageVO", pageVO);
		model.addAttribute("boardList", boardList);

		return "/board/inquiryBoardList";
	}	

}
