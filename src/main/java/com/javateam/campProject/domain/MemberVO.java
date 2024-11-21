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
public class MemberVO {

	private String id;
	private String pw;
	private String name;
	private String gender;
	private String email;

	private String phone;
	private String zip;
	private String roadAddress;
	private String jibunAddress;
	private String detailAddress;

	private Date   birthday;
	private Date   joindate;
	private String membertype;
	private String businessnum;

	@Override
	public boolean equals(Object object) {

		// 같은 객체인지 비교
		if (this == object)
			return true;

		// 비교 대상이 null인지 확인
		if (object == null)
			return false;

		// 클래스 타입이 같은지 확인
		if (getClass() != object.getClass())
			return false;

		MemberVO other = (MemberVO) object;
		return Objects.equals(id, other.id)                     && Objects.equals(pw, other.pw)
			&& Objects.equals(name, other.name)                 && Objects.equals(gender, other.gender)
			&& Objects.equals(email, other.email)               && Objects.equals(phone, other.phone)
			&& Objects.equals(zip, other.zip)                   && Objects.equals(roadAddress, other.roadAddress)
			&& Objects.equals(jibunAddress, other.jibunAddress) && Objects.equals(detailAddress, other.detailAddress)
			&& Objects.equals(birthday, other.birthday)         && Objects.equals(membertype, other.membertype)
			&& Objects.equals(businessnum, other.businessnum);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id,       pw,         name,        gender,       email,
							phone,    zip,        roadAddress, jibunAddress, detailAddress,
							birthday, membertype, businessnum);
	}
}
