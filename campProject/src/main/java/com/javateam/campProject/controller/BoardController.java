package com.javateam.campProject.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.javateam.campProject.domain.BoardDTO;
import com.javateam.campProject.domain.BoardVO;
import com.javateam.campProject.domain.SessionUser;
import com.javateam.campProject.service.BoardService;
import com.javateam.campProject.service.FileUploadService;
import com.javateam.campProject.util.FileUploadUtil;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("board")
@Slf4j
public class BoardController {
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	FileUploadService fileUploadService;
	
	@Value("${spring.servlet.multipart.max-file-size}")
	String uploadFileMaxSize;
	
	@GetMapping("/inquiryBoardWrite")
	public String inquiryBoardWrite(Model model) {
		
		log.info("[BoardController][inquiryBoardWrite]");
		
		model.addAttribute("boardDTO", new BoardDTO());
		return "/board/inquiryBoardWrite";
	}
	
	// DTO 대신 Map 형태의 인자 수신
	@PostMapping("/inquiryBoardWriteProc")
	public String inquiryBoardWriteProc(@RequestParam Map<String, Object> map,
							@RequestPart(value="boardFile") MultipartFile boardFile, Model model) {
		
		log.info("[BoardController][inquiryBoardWriteProc]");  // 게시글 쓰기 인자 전송 현황
		map.entrySet().forEach(arg -> {
			log.info("[inquiryBoardWriteProc][arg]: {}", arg);
		});
		
		BoardVO boardVO = new BoardVO(map, boardFile); // Map → VO

		// 첨부 파일이 있다면...
		String msg = ""; // 메시지
		
		if (boardFile.isEmpty() == false) {

			log.info("게시글 작성 처리(첨부 파일) : {}", boardFile.getOriginalFilename());

			String actualUploadFilename = FileUploadUtil.encodeFilename(boardFile.getOriginalFilename());
			boardVO.setBoardFile(actualUploadFilename);

			// 첨부 파일이 있을 때만 저장
			msg = fileUploadService.storeUploadFile(boardVO.getBoardNum(), boardFile, boardVO.getBoardFile());
			log.info("[inquiryBoardWriteProc][msg] : {}", msg);
		}
		
		log.info("[inquiryBoardWriteProc][BoardVO] : {}", boardVO);

		boardVO = boardService.insertBoard(boardVO);

		log.info("[inquiryBoardWriteProc][게시글 저장 BoardVO] : {}", boardVO);
		
		if (boardVO != null) {
			msg = "게시글 저장에 성공하였습니다.";
		}
		
		model.addAttribute("errMsg", msg);
		model.addAttribute("movePage", "/board/inquiryBoardList");

		return "/error";

	}
	
	@GetMapping("/inquiryBoardView/{boardNum}")
	public String inquiryBoardView(@PathVariable("boardNum") int boardNum, Model model, HttpSession session) { // 세션 추가

		log.info("[BoardController][inquiryBoardView]"); // 게시글 보기
		
		BoardVO boardVO = boardService.selectBoard(boardNum);
		log.info("[inquiryBoardView][BoardVO] : {}", boardVO);
		
		model.addAttribute("board", boardVO);

		// 조회할 때마다 조회수 갱신(+)
		boardService.updateBoardReadcountByBoardNum(boardNum);
		
		// Naver & Google 로그인에 따른 게시글 작성자 변수 변경 적용
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		String realWriter = "";

		if (session.getAttribute("user") != null) { // Naver & Google

			SessionUser sessionUser = (SessionUser)session.getAttribute("user");
			realWriter = sessionUser.getName();

		} else { // 자체 로그인
			realWriter = auth.getName();
		}

		model.addAttribute("realWriter", realWriter);
		
//		return "/board/inquiryBoardView";
		return "/board/inquiryBoardView_no_pw";
	}

}
