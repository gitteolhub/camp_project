package com.javateam.campProject.repository;

import java.util.List;

import com.javateam.campProject.domain.CampReservationVO;

public interface CampReservationDAO {

	// 예약정보 데이터베이스에 추가
	boolean insertReservation(CampReservationVO objCampReservationVO);

	// 개인 예약정보 조회
	CampReservationVO selectReservation(CampReservationVO objCampReservationVO);

	// 개인 예약정보 리스트 조회
	List<CampReservationVO> selectReservationList(CampReservationVO objCampReservationVO);

	// 개인 예약정보 삭제
	void deleteReservation(int id);

	// 전체 예약정보 리스트 조회(CEO용)
	List<CampReservationVO> selectAllReservationList();

	// 캠핑장별 전체 예약현황 수정
	void updateReservation(int id, int availSite);

}
