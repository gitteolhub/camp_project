package com.javateam.campProject.service;

import java.sql.Date;
import java.util.List;

import com.javateam.campProject.domain.CampReservationVO;
import com.javateam.campProject.domain.CampTotReservationVO;

public interface CampReservationService {

	// 예약정보 데이터베이스에 추가
	boolean insertReservation(CampReservationVO objCampReservationVO, String memberId, String socialUserId, int campCNO,
							  Date checkInDate, Date checkOutDate);

	// 개인 예약정보 조회
	CampReservationVO selectReservation(CampReservationVO objCampReservationVO);

	// 캠핑장 사이트수 조회 후 예약가능 여부 점검
	boolean isEnableReservation(CampReservationVO objCampReservationVO);

	// 개인 예약정보 리스트 조회
	List<CampReservationVO> selectReservationList(CampReservationVO objCampReservationVO);

	// 개인 예약정보 삭제
	boolean deleteReservation(int id);

	// 전체 예약정보 리스트 조회(CEO용)
	List<CampReservationVO> selectAllReservationList();

	// 캠핑장별 전체 예약현황 수정
	boolean updateReservation(int id, int availSite);

	// 캠핑장별 전체 예약현황 조회
	CampTotReservationVO selectReservationById(int id);

}
