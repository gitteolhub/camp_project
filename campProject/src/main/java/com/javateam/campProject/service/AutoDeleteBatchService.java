package com.javateam.campProject.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.campProject.domain.BoardUploadFile;
import com.javateam.campProject.domain.BoardVO;
import com.javateam.campProject.repository.FileDeleteMyBatisDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@EnableScheduling
@Slf4j
public class AutoDeleteBatchService {
	
	@Autowired
	FileDeleteMyBatisDAO fileDeleteMyBatisDAO;

	@Autowired
	BoardService boardService;;

	@Autowired
	ImageService imageService;

	@Autowired
	FileService fileService;
	
	@Value("${imageUpload.path}") // 이미지 저장 경로
	String uploadPath;
	
	@Scheduled(cron="0 0 4 * * *") // 매일 오전(심야:유휴시간) 04:00:00에 적용
	// @Scheduled(cron="50 54 16 4 9 *") // 9월 4일 오후 16:54:50에 적용
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	// @Scheduled(cron="0 0/5 * * * ?") // 5분 단위 반복 실행
	public void deleteGarbageImagesAuto() {
		
		// 이미지 파일 지역 저장소의 파일명 현황 확보
		log.info("[AutoDeleteBatchService][파일 지역 저장소 경로]: {}", uploadPath);
		
		// 하위 디렉토리(폴더)들
		List<File> subDirectories = new ArrayList<>();
		
		// 하위 경로들 확보
		try (Stream<Path> uploadPathStream = Files.list(Paths.get(uploadPath))) {

			subDirectories = uploadPathStream.map(Path::toFile)
									   .filter(File::isDirectory)
									   .toList();

			subDirectories.forEach(x -> { log.info("하위 경로들 : {}", x.toString()); });

		} catch (IOException ex) {
			log.error("[AutoDeleteBatchService][파일 경로 탐색 오류]: {}", ex);
		}
		
		List<File> subFiles = new ArrayList<>();

		for (File subDir : subDirectories) {
			
			// 하위 경로들에 따른 저장 이미지 파일이름들 확보
			try (Stream<Path> uploadFileStream = Files.list(Paths.get(subDir.getPath()))) {

				subFiles.addAll(uploadFileStream.map(Path::toFile)
										   		.filter(File::isFile).toList());

				subFiles.forEach(x -> { log.info("하위 경로의 파일들 : {}", x.toString()); });
				
			} catch (IOException ex) {
				log.error("[AutoDeleteBatchService][파일 경로 탐색 오류]: {}", ex);
			}
			
		}
		List<BoardVO> defaultBoardList = boardService.findAll(); // 전체 데이터 조회

		log.info("[AutoDeleteBatchService][defaultBoardList 크기]: {}", defaultBoardList.size());
		
		List<Integer> defaultImgList = new ArrayList<>();
		
		// 게시글 레코드들에서  board_content 필드가 보유하고 있는 등록 이미지 리스트를 확보
		for (BoardVO boardVO : defaultBoardList) {
			defaultImgList.addAll(imageService.getImageList(boardVO.getBoardContent(), "/board/image/"));
		}
		
		log.info("[AutoDeleteBatchService][defaultImgList]: {}", defaultImgList.size());

		// 여기서 구한 아이디(UPLOAD_FILE_TBL.ID)를 제외한 모든 아이디는 가비지 이미지(공식 등록되지 않은 이미지)이므로 모두 삭제
		defaultImgList.forEach(x -> { log.info("기존 DB UPLOAD_FILE_TBL 테이블 이미지 아이디(ID) : " + x); });

		// 해당 upload_file_tbl의 이미지 아이디(PK:기본키) 이외의 레코드(정식 미등록 이미지)는 삭제
		List<BoardUploadFile> realUpdateFileList = fileService.findAll();

		// UPLOAD_FILE_TBL에서 지워야될 이미지 아이디 목록 조회
		List<Integer> deleteImageIdList = new ArrayList<>();
		deleteImageIdList.addAll(realUpdateFileList.stream()
													.map(x -> x.getId())
													.filter(x -> !defaultImgList.contains(x))
													.toList());
		
		deleteImageIdList.forEach(x -> { log.info("삭제할 DB UPLOAD_FILE_TBL 테이블 이미지 아이디(ID) : " + x); });

		// fileDAO.deleteAllById(deleteImageIdList);
		// JPA 결함으로 인해 MyBatis 메서드 활용
		for (int id : deleteImageIdList) {
			fileDeleteMyBatisDAO.deleteFile(id);
			log.info("[AutoDeleteBatchService][id] = {} 레코드 삭제", id);
		}
		
		log.info("[AutoDeleteBatchService][삭제 후]");
		
		// 가비지 이미지 DB table 레코드 삭제 이후 실제 보유 현황 조회
		realUpdateFileList = fileService.findAll();

		realUpdateFileList.forEach(x -> { log.info("삭제 후 기존 DB UPLOAD_FILE_TBL 테이블 이미지 아이디(ID) : " + x); });

		List<String> realUpdateFilenameList = realUpdateFileList.stream().map(BoardUploadFile::getFilePath).toList();

		// 실제 DB table(UPLOAD_FILE_TBL)에 등록된 이미지 파일 리스트
		realUpdateFilenameList.forEach(x -> { log.info("실제 등록된 파일들 : {}", x); });
		
		// 이미지 파일 지역 저장소 파일 현황과 실제 DB table(UPLOAD_FILE_TBL)에 등록된 이미지 파일 현황의 차이
		// : 일괄 삭제할 파일들

		List<String> subFileList = new ArrayList<>();

		// 파일 경로 구분자를 변경 : ex) "\" => "/"
		subFileList.addAll(subFiles.stream().map(x -> x.toString().replace("\\", "/")).toList());

		log.info("[AutoDeleteBatchService][하위 경로들에 따른 저장 이미지 파일수]: {}", subFileList.size());
		log.info("[AutoDeleteBatchService][실제 DB table에 등록된 이미지 파일수]: {}", realUpdateFilenameList.size());
		
		// 차집합 개념 적용
		// : 실제 DB table에 등록된 이미지 파일 리스트 - 하위 경로들에 따른 저장 이미지 파일 리스트

		subFileList.removeAll(realUpdateFilenameList);
		
		log.info("[AutoDeleteBatchService][실제 삭제할 파일(garbage image file) 수]: {}", subFileList.size());

		if (subFileList.size() > 0) {

			subFileList.forEach(x ->  { log.info("실제 삭제할 파일들 : {}", x); } );

			// 파일들 일괄 삭제
			for (String subFile : subFileList) {

				try {
					Files.delete(Paths.get(subFile));
				} catch (IOException ex) {
					log.error("[AutoDeleteBatchService][가비지(garbage) 이미지 파일 삭제 오류]: {}", ex);
				}
			}

		} else {
			log.info("[AutoDeleteBatchService][삭제할 파일들이 없습니다.]");
		}
	}

}
