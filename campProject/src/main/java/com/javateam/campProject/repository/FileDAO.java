package com.javateam.campProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.javateam.campProject.domain.BoardUploadFile;

public interface FileDAO extends JpaRepository<BoardUploadFile, Integer>{
	
	public BoardUploadFile findOneByFileName(String fileName);
	
	public BoardUploadFile findOneById(int id);
	
	public void deleteById(int id);
	
	void deleteAllById(Iterable<? extends Integer> ids);
	
}
