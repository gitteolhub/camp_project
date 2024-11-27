package com.javateam.campProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.javateam.campProject.domain.BoardUploadFile;
import com.javateam.campProject.service.ImageService;
import com.javateam.campProject.util.MediaUtil;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("board")
@Slf4j
public class BoardImageRestController {
	
	@Autowired
	ImageService imageService;
	
	@Autowired
	ServletContext servletContext;
	
	// 이미지 파일 로딩(읽어오기)
	@GetMapping("/image/{fileId}")
	public ResponseEntity<?> serveFile(@PathVariable int fileId) {
		
		log.info("[BoardImageRestController][serveFile]");
		
		try {
			BoardUploadFile boardUploadFile = imageService.load(fileId);
			HttpHeaders httpHeaders = new HttpHeaders();
			
			String fileName = boardUploadFile.getFileName();
			httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" 
            		+ new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");
			
			if (MediaUtil.containsImageMediaType(boardUploadFile.getContentType())) {
                
				httpHeaders.setContentType(MediaType.valueOf(boardUploadFile.getContentType()));
                
            } else {
            	
            	httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }
			
			Resource resource = imageService.loadAsResource(boardUploadFile.getSaveFileName());
            return ResponseEntity.ok().headers(httpHeaders).body(resource);
            
		} catch (Exception ex) {
			log.error("[BoardImageRestController][serveFile][Exception]: {}", ex);
			return ResponseEntity.badRequest().build();
		}
		
	}
	
	// 이미지 파일 저장
	@PostMapping("/image")
	public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
    	
    	log.info("[BoardImageRestController][handleFileUpload]");  // summernote 이미지 파일 업로드 처리
	
    	try {
    		BoardUploadFile boardUploadFile = imageService.store(file);
    		return ResponseEntity.ok().body(servletContext.getContextPath() + "/board/image/" 
    				+ boardUploadFile.getId());
    		
    	} catch (Exception ex) {
    		log.error("[BoardImageRestController][handleFileUpload][Exception]: {}", ex);
    		return ResponseEntity.badRequest().build();
    	}
	}
    	
}
