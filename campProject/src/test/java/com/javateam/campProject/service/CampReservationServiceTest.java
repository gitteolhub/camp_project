package com.javateam.campProject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.campProject.domain.CampReservationVO;

import java.sql.Date;
import java.text.ParseException;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class CampReservationServiceTest {

	@Autowired
	CampReservationService campReservationService;

	private Date convertStringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        try {
        	java.util.Date utilDate = formatter.parse(dateString);
            return new Date(utilDate.getTime());
        } catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

	@Test
	void test() {

		CampReservationVO reservationVO = new CampReservationVO();
        String memberId = null;
        String socialUserId = "49";
        int    campCNO      = 3496 ;
        Date   checkInDate  = convertStringToDate("2024.12.1");
        Date   checkOutDate = convertStringToDate("2024.12.2");

        boolean result = campReservationService.insertReservation(reservationVO, memberId, socialUserId, campCNO, checkInDate, checkOutDate);

        assertTrue(result);
//        assertEquals("홍길동", reservationVO.getUserName());
//        assertEquals("tgdcom7@naver.com", reservationVO.getUserEmail());
//        assertEquals("010-1111-1111", reservationVO.getUserPhone());
	}



}
