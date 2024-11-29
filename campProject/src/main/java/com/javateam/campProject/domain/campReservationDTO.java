package com.javateam.campProject.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class campReservationDTO {

	private int campCNo;

	private String campName;

	@DateTimeFormat(pattern="yyyy.MM.dd")
	private Date checkIn;

	@DateTimeFormat(pattern="yyyy.MM.dd")
	private Date checkOut;

	private String userName;

	private String userEmail;

	private String userPhone;

}
