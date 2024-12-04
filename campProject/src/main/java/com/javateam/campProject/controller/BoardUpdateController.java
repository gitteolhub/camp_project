package com.javateam.campProject.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.javateam.campProject.domain.BoardVO;
import com.javateam.campProject.domain.BoardUploadFile;
import com.javateam.campProject.service.BoardService;
import com.javateam.campProject.service.FileUploadService;
import com.javateam.campProject.service.ImageService;
import com.javateam.campProject.service.ImageStoreService;
import com.javateam.campProject.util.FileUploadUtil;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@SessionAttributes("boardUpdateDTO")
@Controller
@RequestMapping("board")
@Slf4j
public class BoardUpdateController {

	@Autowired
	BoardService boardService;

	@Autowired
	FileUploadService fileUploadService;

	@Autowired
	ImageService imageService;

	@Autowired
	ImageStoreService imageStoreService;

	@GetMapping("/inquiryBoardUpdate")
	public String inquiryBoardUpdate(@RequestParam("boardNum") int boardNum, Model model, HttpSession session) {

		log.info("[BoardUpdateController][inquiryBoardUpdate]");

		BoardVO boardVO = boardService.selectBoard(boardNum);
		// BoardDTO boardDTO = new BoardDTO(boardVO);
		// model.addAttribute("boardDTO", boardDTO);

		log.info("[inquiryBoardUpdate][boardVO]: {}", boardVO);

		// 기존 정보 세션 생성
		if (session.getAttribute("boardUpdateSess") == null) {
			boardVO = boardService.selectBoard(boardNum);
			session.setAttribute("boardUpdateSess", boardVO);
		}

		log.info("[inquiryBoardUpdate][boardVO_2]: {}", boardVO);
		model.addAttribute("board", boardVO);
		// model.addAttribute("boardUpdateDTO", new BoardDTO());

		return "/board/inquiryBoardUpdate";
	} //

	@PostMapping("/inquiryBoardUpdateProc")
	public String inquiryBoardUpdateProc(// @ModelAttribute("boardUpdateDTO") BoardDTO boardUpdateDTO,
							@RequestParam Map<String, Object> map,
							@RequestParam("boardFile") MultipartFile boardFile,
							Model model,
							HttpSession session) {

		log.info("[BoardUpdateController][inquiryBoardUpdateProc]");
		log.info("[inquiryBoardUpdateProc][인자 현황]");

		map.entrySet().forEach(x->{ log.info("{}", x);});

		// 기존 첨부 파일 삭제 여부(추가)
		// log.info("기존 첨부 파일 삭제 여부 : " + map.get("defaultFileDeleteYN"));

		boolean defaultFileDeleteYN = map.get("defaultFileDeleteYN") == null ? false
								    : Boolean.parseBoolean(map.get("defaultFileDeleteYN").toString());

		log.info("[inquiryBoardUpdateProc][첨부 파일 비어 있는지 여부]: {}", boardFile.isEmpty());
		log.info("[inquiryBoardUpdateProc][첨부 파일명]: {}", boardFile != null ? boardFile.getOriginalFilename() : "");

		// 수정에 실패했을 때는  글수정 화면으로 이동하고, 성공하였을 때는 개별 게시글 보기로 이동하도록 변경
		// 성공/실패에 따라 선택적으로 화면 이동하기 위해 변수 활용(movePage)
		// 초기 기본값 변경
		String returnPath; // 글수정 "성공/실패" 모두 "/error/error"로 가도록 재설정
		String movePage = "/board/inquiryBoardUpdate?boardNum=" + map.get("boardNum").toString(); // 리턴(이동) 페이지

		String msg = ""; // 메시지

		map.entrySet().forEach(x -> {
			log.info(x + "");
		});

		BoardVO defaultBoardVO = (BoardVO) session.getAttribute("boardUpdateSess");
		BoardVO updateBoardVO = new BoardVO(map);

		log.info("[inquiryBoardUpdateProc][boardUpdateSession(기존 정보)]: {}", defaultBoardVO);
		log.info("[inquiryBoardUpdateProc][수정 정보]: {}", updateBoardVO);

		// 첨부 파일 처리
		if (boardFile.isEmpty() == false) { // 첨부 파일이 있다면

			// 저장용 파일명 암호화
			String actualUploadFilename = FileUploadUtil.encodeFilename(boardFile.getOriginalFilename());
			updateBoardVO.setBoardOriginalFile(boardFile.getOriginalFilename());
			updateBoardVO.setBoardFile(actualUploadFilename);

			// 신규 업로드 파일 저장(업로드)
			msg = fileUploadService.storeUploadFile(updateBoardVO.getBoardNum(), boardFile,
					updateBoardVO.getBoardFile());
			log.info("[inquiryBoardUpdateProc][msg]: {}", msg);

			// 기존 첨부 파일 삭제
			msg += fileUploadService.deleteUploadFile(defaultBoardVO.getBoardFile());

			log.info("[inquiryBoardUpdateProc][msg2]: {}", msg);

		} else { // 첨부 파일이 없다면

			log.info("[inquiryBoardUpdateProc][첨부 파일이 없다면]");

			// 기존 첨부 파일 지우기
			if (defaultFileDeleteYN == true) {

				// 기존 첨부 파일 삭제
				msg += fileUploadService.deleteUploadFile(defaultBoardVO.getBoardFile());

			} else {

				// 기존 파일을 입력
				String originalFilename = defaultBoardVO.getBoardOriginalFile() != null
						? defaultBoardVO.getBoardOriginalFile()
						: null;
				updateBoardVO.setBoardOriginalFile(originalFilename);

				String encBoardFilename = defaultBoardVO.getBoardFile() != null ? defaultBoardVO.getBoardFile() : null;
				updateBoardVO.setBoardFile(encBoardFilename);
			}

		}

		// 글내용(boardContent) 비교 : 변경시에는 기존 삽입 이미지 삭제 등 처리
		log.info("[inquiryBoardUpdateProc][기존 글내용]: {}", defaultBoardVO.getBoardContent());
		log.info("[inquiryBoardUpdateProc][수정 글내용]: {}", updateBoardVO.getBoardContent());

		// 글내용이 실제로 변경되었다면... (서로 내용이 다른 경우)
		if (defaultBoardVO.getBoardContent().trim().equals(updateBoardVO.getBoardContent().trim()) == false) {

			// 기존 데이터의 삽입 이미지 목록(삽입 이미지 테이블(upload_file_tbl)의 PK(기본키)) 확보

			List<Integer> defaultImgList = imageService.getImageList(defaultBoardVO.getBoardContent().trim(),
					"/board/image/");
			List<Integer> updateImgList = imageService.getImageList(updateBoardVO.getBoardContent().trim(),
					"/board/image/");

			for (int image : defaultImgList) {
				log.info("[inquiryBoardUpdateProc][기존 업로드 이미]: {}", image);
			} //

			for (int image : updateImgList) {
				log.info("[inquiryBoardUpdateProc][신규 업로드 이미지]: {}", image);
			} //

			// 삭제할 글내용에 삽입 이미지 목록
			List<Integer> deleteExpectedImgList = new ArrayList<>();

			// 기존에 이미지가 되어 있지만
			// 신규에는 이미지가 없을 때는 기존 이미지 모두 삭제

			if (updateImgList.size() == 0) {

				log.info("[inquiryBoardUpdateProc][기존 글내용의 모든 이미지 삭제]");
				deleteExpectedImgList.addAll(defaultImgList);

			} else { // 신규에 이미지 포함시 선택 삭제

				log.info("[inquiryBoardUpdateProc][기존글의 이미지들의 선별적 삭제]");

				if  (defaultImgList.size() > 0) {

					for (int image : defaultImgList) {

						if (updateImgList.contains(image) == false) { //

							log.info("[inquiryBoardUpdateProc][실제 삭제할 기존 이미지 기본키(PK)]: {}", image);
							deleteExpectedImgList.add(image);
						}
					}
				}

				// 삭제할 이미지들 출력
				deleteExpectedImgList.forEach(x -> {
					log.info("[inquiryBoardUpdateProc][삭제할 이미지 아이디]: {}", x);
				});

				// 대상 삽입 이미지 파일 삭제 : 삭제할 이미지 있으면 삭제

				if (deleteExpectedImgList.size() > 0) {

					for (int imageId : deleteExpectedImgList) {

						// 삽입 이미지 테이블(INQUIRY_BOARD_UPFILE)에서 저장경로/파일명 가져옴(file_path 필드)
						BoardUploadFile uploadFile = imageService.load(imageId); // 삭제할 이미지 파일 경로 확보 :
																			// uploadFile.getFilePath()
						// 삽입 이미지 삭제 삭제
						log.info("[inquiryBoardUpdateProc][삭제 메시지]: {}", fileUploadService.deleteImageFile(uploadFile.getFilePath()));
						// 삽입 이미지 테이블(upload_file_tbl)에서도 해당 이미지 수록 내용 삭제
						imageStoreService.deleteById(imageId);

						log.info("[inquiryBoardUpdateProc][이미지를 삭제하였습니다.]");
					} // for

				} // if (deleteExpectedImgList.size() > 0) {

			} // if (updateImgList.size() == 0) {

		} else { // 변경 내용이 없다면

			msg = "게시글 수정(변경) 내용이 없습니다.";

		}

		// 등록일 => 최근 수정일로 변경
		updateBoardVO.setBoardDate(new Date(System.currentTimeMillis()));

		log.info("[inquiryBoardUpdateProc][최종 게시글 수정 내용]: {}", updateBoardVO);

		// 게시글 수정
		BoardVO resultVO = boardService.updateBoard(updateBoardVO);

		if (resultVO == null) {

			msg = "게시글 수정에 실패하였습니다.";

		} else {

			log.info("[inquiryBoardUpdateProc][최종 저장 결과]: {}", resultVO);
			msg = "게시글 수정에 성공하였습니다.";

			// 수정에 실패했을 때는  글수정 화면으로 이동하고, 성공하였을 때는 개별 게시글 보기로 이동하도록 변경
			// 게시글 수정 성공후 개별 게시글 보기로 이동
			movePage = "/board/inquiryBoardView/" + map.get("boardNum").toString();
		} //
		model.addAttribute("errMsg", msg);

		// 수정에 실패했을 때는  글수정 화면으로 이동하고, 성공하였을 때는 개별 게시글 보기로 이동하도록 변경
		// 초기값은 메서드 초기에 언급된 지역 변수에서 변경(movePage)
		model.addAttribute("movePage", movePage);
		returnPath = "/error"; // 에러 페이지로 이동

		return returnPath;
	}
}