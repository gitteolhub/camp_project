package com.javateam.campProject.service;

import java.sql.Date;

import com.javateam.campProject.domain.CampReservationVO;

public interface CampReservationService {

	// 예약정보 데이터베이스에 추가
	boolean insertReservation(CampReservationVO objCampReservationVO, String memberId, String socialUserId, int campCNO,
							  Date checkInDate, Date checkOutDate);

	// 체크인, 체크아웃 날짜 변환(String)
//	Date convertStringToDate(String dateString);
}
