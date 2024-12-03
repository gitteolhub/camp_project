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

}
