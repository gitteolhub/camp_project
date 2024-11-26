package com.javateam.campProject.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.javateam.campProject.domain.BoardUploadFile;
import com.javateam.campProject.util.FileUploadUtil;

import lombok.extern.slf4j.Slf4j;


// 글 내용 중 삽입 이미지 파일 관련 서비스(파일 쓰기/읽기/삭제)
@Service
@Slf4j
public class ImageService {
	
	private final Path rootLocation;
	
	@Autowired
	public ImageService(@Value("${imageUpload.path}") String uploadPath) {
		
		log.info("[ImageService][uploadPath]: {}", uploadPath);
		this.rootLocation = Paths.get(uploadPath);
	}
	
	@Autowired
	ImageStoreService imageStoreService;
	
	// 모든 파일 읽어오기
	public Stream<Integer> loadAll() {

		log.info("[ImageService][loadAll]");
		
        List<BoardUploadFile> files = imageStoreService.findAll();
        return files.stream().map(file -> file.getId());
    }
	
	// 파일 읽어오기
	public BoardUploadFile load(int fileId) {

		log.info("[ImageService][load]");
		 
	    return imageStoreService.findOneById(fileId);
	}
	
	// 파일 자원(resource)로딩
	public Resource loadAsResource(String fileName) throws Exception {
		 log.info("[ImageService][loadAsResource]");
		 
		 try {

            if (fileName.toCharArray()[0] == '/') {
                fileName = fileName.substring(1);
            }

            Path file = loadPath(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new Exception("파일을 읽을 수 없습니다 : " + fileName);
            }

        } catch (Exception ex) {
            throw new Exception("파일을 읽을 수 없습니다 : " + fileName);
        }
		 
	}
	
	// 경로 읽어오기
	private Path loadPath(String fileName) {
		
		log.info("[ImageService][loadPath]");
		
        return rootLocation.resolve(fileName);
    }
	
	// 업로드 파일 저장
	public BoardUploadFile store(MultipartFile file) throws Exception {
		
		log.info("[ImageService][store]");
		
		try {
            if (file.isEmpty()) {
                throw new Exception("업로드 파일이 비어 있어서 저장에 실패하였습니다 : " + file.getOriginalFilename());
            }

            String saveFileName = FileUploadUtil.fileSave(rootLocation.toString(), file);

            if (saveFileName.toCharArray()[0] == '/') {
                saveFileName = saveFileName.substring(1);
            }

            Resource resource = loadAsResource(saveFileName);
            
            // 업로드 파일 객체 구성
            BoardUploadFile saveFile = new BoardUploadFile();
            
            saveFile.setSaveFileName(saveFileName);
            saveFile.setFileName(file.getOriginalFilename());
            saveFile.setContentType(file.getContentType());
            
            log.info("[store][Root 경로]: {}", rootLocation);
            
            String tempPath = rootLocation.toString()
            		.replace(File.separatorChar, '/') + File.separator + saveFileName;
            saveFile.setFilePath(tempPath.replace("\\", "/"));
            
            log.info("[store][이미지 파일 저장경로]: {}", saveFile.getFilePath());
            
            saveFile.setFileSize(resource.contentLength());
            saveFile.setRegDate(new Date());
            saveFile = imageStoreService.save(saveFile); // 저장

            return saveFile;
            
		} catch (IOException ex) {
			throw new Exception("Failed to store file " + file.getOriginalFilename(), ex);
		}

	}
	
	// 게시글내의 삽입 이미지 리스트 조회
	public List<Integer> getImageList(String str, String imgUploadPath) {
		
		log.info("[BoardService][getImageList]");
		List<Integer> imgList = new ArrayList<>();
		
		if (str.contains(imgUploadPath) == false) { // 이미지 미포함

			log.info("[getImageList][이미지가 전혀 포함되어 있지 않습니다.]");

		} else {
			// 포함된 전체 이미지 수 : 이 한계량 만큼 검색  => 카운터에 반영
			int imgLen = StringUtils.countOccurrencesOf(str, imgUploadPath);

			log.info("[getImageList][imgLen]: {}", imgLen);
			
			// 이미지 검색 카운터 설정 : 이미지 검색할 횟수
			int count = 0;

			int initPos = str.indexOf(imgUploadPath);
			log.info("[getImageList][첫 발견 위치]: {}", initPos);
			
			// 추출된 문자열 : 반복문에서 사용
			String subStr = str;
			
			while (count < imgLen) {

				initPos = subStr.indexOf(imgUploadPath);

				// 이미지 파일만 추출 (첫번째)
				// "/board/image/".length()
				initPos += imgUploadPath.length();
				log.info("[getImageList][이미지 파일 시작 위치]: {}", initPos);
				
				// 추출된 문자열
				// ex) 41 (.../board/image/41" : INQUIRY_BOARD_UPFILE 테이블의 삽입 이미지 PK(기본키))
				subStr = subStr.substring(initPos);

				log.info("[getImageList][subStr]: {}", subStr);
				
				// 첫번째 " (큰 따옴표) 위치 검색하여 순수한 숫자(PK)만 추출
				int quotMarkPos = subStr.indexOf("\"");

				// 이미지 파일 끝 검색하여 이미지 파일명/확장자 추출
				// 이미지 끝 검색 : 검색어(" )
				int imgFileNum = Integer.parseInt(subStr.substring(0, quotMarkPos));

				log.info("[getImageList][이미지 파일 테이블 PK(기본기)]: {}", imgFileNum);
				
				count++; // 이미지 추출되었으므로 카운터 증가

				imgList.add(imgFileNum); // 리스트에 추가
				
				log.info("[getImageList][//while]");
			}

		}
		
		return imgList;
	}
}
