package com.javateam.campProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javateam.campProject.domain.BoardUploadFile;
import com.javateam.campProject.domain.BoardVO;
import com.javateam.campProject.service.BoardService;
import com.javateam.campProject.service.FileUploadService;
import com.javateam.campProject.service.ImageService;
import com.javateam.campProject.service.ImageStoreService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("board")
@Slf4j
public class BoardDeleteController {
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	FileUploadService fileUploadService;

	@Autowired
	ImageService imageService;

	@Autowired
	ImageStoreService imageStoreService;
	
	// Naver & Google 적용
	@GetMapping("/deleteProc")
	public String deleteProc(@RequestParam("boardNum") int boardNum, Model model) {
		
		log.info("[BoardDeleteController][boardNum]");	
		log.info("[deleteProc][boardNum]: {}", boardNum);	
		
		// 개별 게시글 보기로 이동(movePage)
		// 게시글 삭제 성공시에는 게시글 목록으로 이동(이미 삭제되었으므로 이동할 게시글이 없음)
		// 게시글 삭제 실패시에는 게시글 보기로 이동
		String returnPath; // 글삭제 "성공/실패" 모두 "/error/error"로 가도록 재설정
		String movePage = "/board/inquiryBoardView/" + boardNum; // 리턴(이동) 페이지

		String msg = ""; // 메시지
		
		BoardVO boardVO = boardService.selectBoard(boardNum); // 기존 정보 읽어오기

		log.info("[deleteProc][기존 정보][boardVO]: {}", boardVO);
		
		// 댓글들 현황 점검
		// 댓글들이 있다면 삭제 못하도록 차단 !
		// 댓글이 없을 경우에 삭제 허용 !
		if (boardService.selectBoardsCountWithReplies(boardNum) > 0) { // 댓글(들)이 있을 경우

//			msg = "댓글이 있는 원글은 삭제할 수 없습니다.";
			log.info("[deleteProc][댓글 있는 원글]");
			
			// 삭제할 삽입 이미지 점검
			List<Integer> deleteImgList = imageService.getImageList(boardVO.getBoardContent().trim(),
					"/board/image/");

			for (int deleteImg : deleteImgList) {
				log.info("[deleteProc][삭제할 업로드  이미지] : " + deleteImg);
			} //
			
			// 삽입 이미지들 삭제
			if (deleteImgList.size() > 0) { // 삭제할 이미지들이 있다면...

				for (int imageId : deleteImgList) {

					BoardUploadFile uploadFile = imageService.load(imageId); // 삭제할 이미지 파일 경로 확보 :
					// 삽입 이미지 삭제 삭제
					log.info("삭제 메시지 : {}", fileUploadService.deleteImageFile(uploadFile.getFilePath()));
					// 삽입 이미지 테이블(upload_file_tbl)에서도 해당 이미지 수록 내용 삭제
					imageStoreService.deleteById(imageId);

				} // for

			} // if (deleteImgList.size() > 0)
			
			log.info("[deleteProc][첨부 파일 삭제 직전]");

			// 첨부 파일 삭제
			if (boardVO.getBoardOriginalFile() != null) { // 첨부 파일이 있다면

				log.info("[deleteProc][첨부 파일 존재]");

				// 기존 첨부 파일 삭제
				msg += fileUploadService.deleteUploadFile(boardVO.getBoardFile());
				log.info("[deleteProc][파일 삭제][msg]: {}", msg);

			}
			
//			int boardReRef = boardVO.getBoardReRef(); // 댓글을 삭제하기 위한 boardReRef 값
			int boardReRef = boardVO.getBoardNum(); // 댓글을 삭제하기 위한 boardReRef 값
	        if (boardReRef > 0) {
	            boolean deleteRepliesSuccess = boardService.deleteByBoardReRef(boardReRef); // 댓글 삭제
	            if (!deleteRepliesSuccess) {
	                msg = "댓글 삭제에 실패하였습니다.";
	            }
	        }
			
	        // 게시글 테이블(board_tbl)에서 게시글 자체를 삭제
	        if (boardService.deleteById(boardNum)) {
	            msg = "게시글 삭제에 성공하였습니다.";
	            movePage = "/board/inquiryBoardList"; // 게시글 목록으로 이동
	        } else {
	            msg = "게시글 삭제에 실패하였습니다.";
	        }
			
		} else { // 댓글이 없을 경우 (삭제 가능)

			log.info("[deleteProc][댓글 없는 원글(삭제 가능한 글)]");
			
			// 삭제할 삽입 이미지 점검
			List<Integer> deleteImgList = imageService.getImageList(boardVO.getBoardContent().trim(),
					"/board/image/");

			for (int deleteImg : deleteImgList) {
				log.info("[deleteProc][삭제할 업로드  이미지] : " + deleteImg);
			} //
			
			// 삽입 이미지들 삭제
			if (deleteImgList.size() > 0) { // 삭제할 이미지들이 있다면...

				for (int imageId : deleteImgList) {

					BoardUploadFile uploadFile = imageService.load(imageId); // 삭제할 이미지 파일 경로 확보 :
					// 삽입 이미지 삭제 삭제
					log.info("삭제 메시지 : {}", fileUploadService.deleteImageFile(uploadFile.getFilePath()));
					// 삽입 이미지 테이블(upload_file_tbl)에서도 해당 이미지 수록 내용 삭제
					imageStoreService.deleteById(imageId);

				} // for

			} // if (deleteImgList.size() > 0)
			
			log.info("[deleteProc][첨부 파일 삭제 직전]");

			// 첨부 파일 삭제
			if (boardVO.getBoardOriginalFile() != null) { // 첨부 파일이 있다면

				log.info("[deleteProc][첨부 파일 존재]");

				// 기존 첨부 파일 삭제
				msg += fileUploadService.deleteUploadFile(boardVO.getBoardFile());
				log.info("[deleteProc][파일 삭제][msg]: {}", msg);

			} // if
			
			// 게시글 테이블(board_tbl)에서 게시글 자체를 삭제
			if (boardService.deleteById(boardNum) == true) {

				msg = "게시글 삭제에 성공하였습니다.";
				movePage = "/board/inquiryBoardList"; // 게시글 목록으로 인동

			} else {

				msg = "게시글 삭제에 실패하였습니다.";

			} //
		}
		
		model.addAttribute("errMsg", msg);

		// 초기값은 메서드 초기에 언급된 지역 변수에서 변경(movePage)
		model.addAttribute("movePage", movePage);
		returnPath = "/error"; // 에러 페이지로 이동

		return returnPath;
	}

}
