package com.javateam.campProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.campProject.domain.BoardVO;
import com.javateam.campProject.repository.BoardDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	BoardDAO boardDAO;

	// 게시글 작성
	@Override
	@Transactional(rollbackFor = Exception.class)
	public BoardVO insertBoard(BoardVO boardVO) {
		
		log.info("[BoardService][insertBoard]");
		
		return boardDAO.save(boardVO);
	}

	// 게시글 갯수 조회
	@Override
	@Transactional(readOnly = true)
	public int selectBoardsCount() {
		
		log.info("[BoardService][selectBoardsCount]");
		
		return (int)boardDAO.count();
	}

	// 게시글 조회(페이징)
	@Override
	@Transactional(readOnly = true)
	public List<BoardVO> selectBoardsByPaging(int currPage, int limit) {
		
		log.info("[BoardService][selectBoardsByPaging]");
		
		Pageable pageable = PageRequest.of(currPage-1, limit, Sort.by(Direction.DESC, "boardNum"));
		return boardDAO.findAll(pageable).getContent();
	}
	
	// 개별 게시글 조회
	@Override
	@Transactional(readOnly = true)
	public BoardVO selectBoard(int boardNum) {
		
		log.info("[BoardService][selectBoard]");
		
		return boardDAO.findById(boardNum);
	}

	// 게시글 검색시 레코드 갯수 조회
	@Override
	@Transactional(readOnly = true)
	public int selectBoardsCountBySearching(String searchKey, String searchWord) {
		
		log.info("[BoardService][selectBoardsCountBySearching]");
		
		return searchKey.equals("board_subject") ? boardDAO.countByBoardSubjectContaining(searchWord) :
		   	   searchKey.equals("board_content") ? boardDAO.countByBoardContentContaining(searchWord) :
		   	   boardDAO.countByBoardWriterContaining(searchWord);
	}

	// 게시글 조회(페이징)
	@Override
	@Transactional(readOnly = true)
	public List<BoardVO> selectBoardsBySearching(int currPage, int limit, String searchKey, String searchWord) {
		
		log.info("[BoardService][selectBoardsBySearching]");
		
		Pageable pageable = PageRequest.of(currPage-1, limit, Sort.by(Direction.DESC, "boardNum"));

		return searchKey.equals("board_subject") ? boardDAO.findByBoardSubjectContaining(searchWord, pageable).getContent() :
			   searchKey.equals("board_content") ? boardDAO.findByBoardContentContaining(searchWord, pageable).getContent() :
			   boardDAO.findByBoardWriterContaining(searchWord, pageable).getContent();
	}

	// 개별 게시글 수정
	@Override
	@Transactional(rollbackFor = Exception.class)
	public BoardVO updateBoard(BoardVO boardVO) {
		
		log.info("[BoardService][updateBoard]");
		
		return boardDAO.save(boardVO);
	}

	// 개별 게시글 댓글 조회
	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<BoardVO> selectReplysById(int boardNum) {
		
		log.info("[BoardService][selectReplysById]");
		
		return boardDAO.findByBoardReRef(boardNum);
	}
	
	// 댓글을 제외한 게시글 갯수 조회
	@Override
	@Transactional(readOnly = true)
	public int selectBoardsCountWithoutReplies() {
		
		log.info("[BoardService][selectBoardsCountWithoutReplies]");
		
		return (int)boardDAO.countByBoardReRef(0); // (댓글 아닌)원글만 추출 : board_re_ref = 0
	}

	// 댓글을 제외한 게시글 리스트 조회(페이징)
	@Override
	@Transactional(readOnly = true)
	public List<BoardVO> selectBoardsByPagingWithoutReplies(int currPage, int limit) {
		
		log.info("[BoardService][selectBoardsByPagingWithoutReplies]");
		
		Pageable pageable = PageRequest.of(currPage-1, limit, Sort.by(Direction.DESC, "boardNum"));
		// return boardDAO.findAll(pageable).getContent();
		return boardDAO.findByBoardReRef(0, pageable).getContent(); // (댓글 아닌)원글만 추출 : board_re_ref = 0
	}

	// 댓글 삭제
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteReplysById(int boardNum) {
		
		log.info("[BoardService][deleteReplysById]");
		boolean result = false;

		try {
			boardDAO.deleteById(boardNum);
			result = true;
		} catch (Exception ex) {
			log.error("[BoardService][deleteReplyById][Exception]: {}", ex);
			result = false;
		}

		return result;
	}

	// 댓글을 제외한 게시글 갯수 조회
	@Override
	@Transactional(readOnly = true)
	public int selectBoardsCountWithReplies(int boardNum) {
		
		log.info("[BoardService][selectBoardsCountWithReplies]");
		
		return (int)boardDAO.countByBoardReRef(boardNum); // 댓글의 갯수 추출 : board_re_ref = boardNum
	}

	// 개별 게시글 삭제
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteById(int boardNum) {
		
		log.info("[BoardService][deleteById]");
		boolean result = false;

		try {
			boardDAO.deleteById(boardNum);
			result = true;
		} catch (Exception ex) {
			log.error("[BoardService][deleteById][Exception] : {}", ex);
			result = false;
		}

		return result;
	}
	
	// 게시글 삭제(댓글이 있을 경우 댓글 먼저 삭제 후 게시글 삭제)
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteByBoardReRef(int boardReRef) {
		
		log.info("[BoardService][deleteByBoardReRef]");
		boolean result = false;

		try {
			boardDAO.deleteByBoardReRef(boardReRef);
			result = true;
		} catch (Exception ex) {
			log.error("[BoardService][deleteByBoardReRef][Exception] : {}", ex);
			result = false;
		}

		return result;
	}
		
	// 게시글 조회수 수정(업데이트)
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateBoardReadcountByBoardNum(int boardNum) {
		
		log.info("[BoardService][updateBoardReadcountByBoardNum]");
		boolean result = false;

		try {
			boardDAO.updateBoardReadcountByBoardNum(boardNum);
			result = true;
		} catch (Exception ex) {
			log.error("[updateBoardReadcountByBoardNum][Exception]: {}", ex);
			result = false;
		}

		return result;
	}

	// 전체 게시글 조회
	@Override
	@Transactional(readOnly = true)
	public List<BoardVO> findAll() {
		
		log.info("[BoardService][findAll]");
		return (List<BoardVO>) boardDAO.findAll(Sort.by("boardNum"));
	}

}
