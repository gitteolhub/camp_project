package com.javateam.campProject.service;

import java.util.List;

import com.javateam.campProject.domain.BoardVO;

public interface BoardService {

	// 게시글 작성
	BoardVO insertBoard(BoardVO boardVO);
	
	// 게시글 갯수 조회
	int selectBoardsCount();
	
	// 게시글 조회(페이징)
	List<BoardVO> selectBoardsByPaging(int currPage, int limit);
	
	// 개별 게시글 조회
	BoardVO selectBoard(int boardNum);
	
	// 게시글 검색시 레코드 갯수 조회
	int selectBoardsCountBySearching(String searchKey, String searchWord);
	
	/**
	 * 게시글 조회 (페이징)
	 *
	 * @param currPage 현재 페이지
	 * @param limit 페이지 당 글수
	 * @param searchKey 검색 구분
	 * @param searchWord 검색어
	 * @return
	 */
	List<BoardVO> selectBoardsBySearching(int currPage, int limit, String searchKey, String searchWord);
	
	// 개별 게시글 수정
	BoardVO updateBoard(BoardVO boardVO);
	
	// 개별 게시글 댓글 조회
	List<BoardVO> selectReplysById(int boardNum);
	
	// 댓글을 제외한 게시글 갯수 조회
	int selectBoardsCountWithoutReplies();
	
	// 댓글을 제외한 게시글 리스트 조회(페이징)
	List<BoardVO> selectBoardsByPagingWithoutReplies(int currPage, int limit);
	
	// 댓글 삭제
	boolean deleteReplysById(int boardNum);
	
	// 댓글을 제회한 게시글 갯수 조회
	int selectBoardsCountWithReplies(int boardNum);
	
	// 개별 게시글 삭제
	boolean deleteById(int boardNum);
	
	// 게시글 삭제(댓글이 있을 경우 댓글 먼저 삭제 후 게시글 삭제)
	boolean deleteByBoardReRef(int boardReRef);
	
	// 게시글 조회수 수정(업데이트)
	boolean updateBoardReadcountByBoardNum(int boardNum);
	
	// 전체 게시글 조회
	List<BoardVO> findAll();
}
