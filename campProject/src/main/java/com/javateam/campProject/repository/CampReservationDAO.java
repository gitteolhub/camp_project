package com.javateam.campProject.repository;

import com.javateam.campProject.domain.CampReservationVO;

public interface CampReservationDAO {

	// 예약정보 데이터베이스에 추가
	boolean insertReservation(CampReservationVO objCampReservationVO);
}
