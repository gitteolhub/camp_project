package com.javateam.campProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.campProject.domain.BoardUploadFile;
import com.javateam.campProject.repository.FileDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
	
	@Autowired
	FileDAO fileDAO;

	@Override
	@Transactional(readOnly = true)
	public List<BoardUploadFile> findAll() {
		
		return fileDAO.findAll();
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void deleteAllById(List<Integer> deleteImageIdList) {
		
		log.info("[FileService][이미지 파일 레코드 삭제]");

		try {

			fileDAO.deleteAllById(deleteImageIdList);

		} catch (Exception ex) {
			log.error("[FileService][삭제 실패]: {}", ex);
		}
		
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void deleteById(int boardNum) {
		
		fileDAO.deleteById(boardNum);
	}

}
