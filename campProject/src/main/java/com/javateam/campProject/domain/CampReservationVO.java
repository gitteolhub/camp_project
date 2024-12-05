package com.javateam.campProject.domain;

import java.sql.Date;
import java.util.Objects;

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

	private int id;
	private int campId;
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

	// true: 예약 가능, false: 예약 불가(중복)
	@Override
	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		CampReservationVO other = (CampReservationVO) obj;
//		return Objects.equals(checkIn, other.checkIn) && Objects.equals(checkOut, other.checkOut)
//				&& Objects.equals(userEmail, other.userEmail) && Objects.equals(userName, other.userName)
//				&& Objects.equals(userPhone, other.userPhone);

		boolean blRetVal = false;

		CampReservationVO other = (CampReservationVO) obj;

		if(Objects.equals(userEmail, other.userEmail)
			&& Objects.equals(userName, other.userName)
			&& Objects.equals(userPhone, other.userPhone)) { // 동일인 경우

			// 날짜가 겹치지 않는 상황
			// this: 날짜1, other: 날짜2
			// 종료일2 < 시작일1 또는
			// 종료일1 < 시작일2
			if(other.checkOut.getTime() < this.checkIn.getTime() ||
			   this.checkOut.getTime()  < other.checkIn.getTime()) {
				blRetVal = true;
			} else {
				blRetVal = false;
			}
		}

		return blRetVal;
	}

	@Override
	public int hashCode() {
		return Objects.hash(checkIn, checkOut, userEmail, userName, userPhone);
	}
}
