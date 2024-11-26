package com.javateam.campProject.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;


// 첨부 파일 관련 서비스(파일 쓰기/읽기/삭제)
@Service
@Slf4j
public class FileUploadService {
	
	// 파일 업로드 경로
	private final Path uploadPath;

	private final int uploadFileMaxSize;
	
	@Autowired
	public FileUploadService(@Value("${fileUpload.path}") String uploadPath,
							 @Value("${spring.servlet.multipart.max-file-size}") String uploadFileMaxSize) {
		
		log.info("[FileUploadService][uploadPath]: {}", uploadPath);
		this.uploadPath = Paths.get(uploadPath);
		// 10M => 10 * 1024 * 1024
		
		int maxSize = 0;
		
		// MB => 숫자 치환
		if (uploadFileMaxSize.contains("MB")) {

			String tempStr = uploadFileMaxSize.substring(0, uploadFileMaxSize.indexOf("MB"));
			maxSize = Integer.parseInt(tempStr) * (int)Math.pow(2, 10) * (int)Math.pow(2, 10);
		}
		
		this.uploadFileMaxSize = maxSize;
	}
	
	/**
	 * 파일 업로드 서비스
	 *
	 * @param boardNum 게시글 번호
	 * @param file 업로드할 파일
	 * @param encodingFilename 업로드될 실제 파일명(암호화 처리)
	 * @return 업로드 결과 메시지
	 */
	public String storeUploadFile(int boardNum, MultipartFile file, String encodingFilename) {
		
		log.info("[FileUploadService][storeUploadFile]");
		
		String result = "";
		FileOutputStream fileOutputStream = null;
		
		log.info("[FileUploadService][uploadPath][자원 경로]: " + uploadPath.toString());
		
		// 업로드 파일 처리
	 	// 첨부 파일이 없을 때
		if (file.isEmpty() || file == null) {

			result = "첨부 파일이 없습니다.";
			log.error("[FileUploadService][error]: {}", result);
		} else { // 파일 유효성 점검

			// 저장 폴더 존재 점검
			if (Files.exists(uploadPath)) {

				log.info("[FileUploadService][파일 업로드 저장소(폴더)가 존재합니다.]");

			} else {
				
				result = "파일 업로드 저장소(폴더)가 존재하지 않습니다.";
				log.error("[FileUploadService][error_2]: {}", result);
				
				// 폴더 생성
				try {
					Files.createDirectory(uploadPath);
				} catch (Exception ex) {
					result = "파일 업로드 저장소(폴더)가 생성되지 않았습니다.";
					log.error("[FileUploadService][error_3]: {}", result);
				}
			}
			
			log.info("[FileUploadService][게시글 번호] : {}", boardNum);
			
			try {
	    	 	// 업로드 파일 형식 변환(시작) : 추가
				log.info("[FileUploadService][실제 업로드 파일명] : {}", encodingFilename);
				// 업로드 파일 형식 변환(끝) : 추가

	    	 	byte[] bytes = file.getBytes();

	    	 	log.info("[FileUploadService][uploadPath_2] : {}", uploadPath.toString());

	            File outFileName = new File(uploadPath.toString() + "/" + encodingFilename);
	            
	            fileOutputStream = new FileOutputStream(outFileName);
	            fileOutputStream.write(bytes);
	            
	            result = "파일이 업로드 되었습니다.";
	            
			} catch (IOException ex) {
				
				result = "파일 처리중 오류가 발생하였습니다. ";
		        log.error("[FileUploadService][error_4]: {}", result);
		        ex.printStackTrace();
		        
			} catch (Exception ex) {
				
				log.error("[FileUploadService][Exception]: {}", ex );
				
			} finally {
				
				try {
					if(fileOutputStream != null) fileOutputStream.close();
					
				} catch(IOException ex) {
					result = "파일 처리중 오류가 발생하였습니다. ";
					log.error("[FileUploadService][storeUploadFile][error]: {}", result);
					log.error("[FileUploadService][IOException]: {}", ex);
				}
			}
			
		}
		return result;
	}

	// 파일 삭제
	public String deleteUploadFile(String encodingFilename) {

		log.info("[FileUploadService][deleteUploadFile]");
		String msg = ""; // 메시지
		
		try {

	 		Files.deleteIfExists(Paths.get(uploadPath + "/" + encodingFilename));
	 		msg = "파일 삭제에 성공하였습니다.";

		} catch (IOException ex) {
			log.error("[deleteUploadFile][업로드 파일 삭제 에러]: {}", ex);
			msg = "파일 삭제에 실패하였습니다.";
		}
		
		return msg;
	}
	
	// 삽입 이미지 파일 삭제 서비스
	// encodingFilename 업로드된 파일 경로 + 파일명
	
	public String deleteImageFile(String encodingFilename) {

		log.info("[FileUploadService][deleteImageFile]");
		String msg = ""; // 메시지
		
		try {

	 		if (Files.deleteIfExists(Paths.get(encodingFilename)) == true) {
	 			msg = "파일 삭제에 성공하였습니다.";
	 		} else {
	 			msg = "파일 삭제에 실패하였습니다.";
	 		}

		} catch (IOException ex) {
			log.error("[deleteImageFile][업로드 파일 삭제 에러]: {}", ex);
			msg = "파일 삭제에 실패하였습니다.";
		}
		
		return msg;
	}
}
