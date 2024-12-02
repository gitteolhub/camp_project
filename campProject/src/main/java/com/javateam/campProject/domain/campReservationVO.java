package com.javateam.campProject.domain;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class CampReservationVO {

	private int campCNo;
	private String campName;
	private Date checkIn;
	private Date checkOut;
	private String userName;
	private String userEmail;
	private String userPhone;


	public void setCheckIn(Date checkIn) {
	    this.checkIn = checkIn;
	}

	public void setCheckOut(Date checkOut) {
	    this.checkOut = checkOut;
	}
}
