package com.javateam.campProject.domain;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BoardDTO {
	
	private int boardNum;
	private String boardWriter;
	private String boardSubject;
	private String boardContent;
	private MultipartFile boardFile; // 게시글 첨부 파일
	
	private int boardReRef; // 게시글 댓글의 원 게시글(관련글) 번호
	private int boardReLev; // 게시글 댓글 레벨
	private int boardReSeq; // 게시글 댓글 순서
	private int boardReadCount = 0;
	private Date boardDate;
	
	private String textMulti = "text"; // 텍스트 모드(text:기본값) / 멀티미디어 모드(multi)
	
	// 업로드 파일(파일명을 확인할 수 있도록 파일명 인쇄) : boardFile.getOriginalFilename()
	@Override
	public String toString() {
		return "BoardDTO [boardNum=" + boardNum + ", boardWriter=" + boardWriter                   + ", boardSubject=" + boardSubject 
			 + ", boardContent=" + boardContent + ", boardFile=" + boardFile.getOriginalFilename() + ", boardReRef=" + boardReRef
			 + ", boardReLev=" + boardReLev     + ", boardReSeq=" + boardReSeq                     + ", boardReadCount=" + boardReadCount 
			 + ", boardDate=" + boardDate       + ", textMulti=" + textMulti + "]";
	}

}