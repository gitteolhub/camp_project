package com.javateam.campProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.campProject.domain.BoardUploadFile;
import com.javateam.campProject.repository.FileDAO;

import lombok.extern.slf4j.Slf4j;

// 글 내용 중 삽입 이미지 파일 관련 서비스
@Service
@Slf4j
public class ImageStoreService {
	
	@Autowired
	FileDAO fileDAO;
	
	 @Transactional(rollbackFor = Exception.class)
	 public BoardUploadFile findOneByFileName(String fileName) {
		 
		 log.info("[ImageStoreService][findOneByFileName]");
		 
		 return fileDAO.findOneByFileName(fileName);
	 }
	 
	 @Transactional(rollbackFor = Exception.class)
	 public BoardUploadFile findOneById(int id) {
		 
		 log.info("[ImageStoreService][findOneById]");
		 
		 return fileDAO.findOneById(id);
	 }
	 
	 @Transactional(readOnly = true)
	 public List<BoardUploadFile> findAll() {
		 
		 log.info("[ImageStoreService][findAll]");
		 
		 return fileDAO.findAll();
		 
	 }
	 
	 @Transactional(rollbackFor = Exception.class)
	 public BoardUploadFile save(BoardUploadFile saveFile) {
		 
		 log.info("[ImageStoreService][save]");
		 
		 return fileDAO.save(saveFile);
	 }
	 
	 @Transactional(rollbackFor = Exception.class)
	 public void deleteById(int id) {
		 
		 log.info("[ImageStoreService][deleteById]");
		 
		 fileDAO.deleteById(id);
	 }
}
