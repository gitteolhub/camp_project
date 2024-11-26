package com.javateam.campProject.service;

import java.util.List;

import com.javateam.campProject.domain.BoardUploadFile;


public interface FileService {
	
	List<BoardUploadFile> findAll();

	void deleteAllById(List<Integer> deleteImageIdList);

	void deleteById(int boardNum);

}
