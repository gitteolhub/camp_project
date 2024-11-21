package com.javateam.campProject.domain;

import java.sql.Date;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MemberVO {

	/** 아이디 */
	private String id;
	/** 패쓰워드 */
	private String pw;
	/** 이름 */
	private String name;
	/** 성별 */
	private String gender;
	/** 이메일 */
	private String email;
	/** 연락처(휴대폰) */
	private String phone;
	/** 우편번호 */
	private String zip;
	/** 도로명 주소 */
	private String roadAddress;
	/** 지번 주소 */
	private String jibunAddress;
	/** 상세 주소 */
	private String detailAddress;
	/** 생년월일 */
	private Date birthday;
	/** 가입일 */
	private Date joindate;
	/** 회원 종류*/
	private String memberType;
	/** 사업자 번호*/
	private String businessNum;


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
		return Objects.equals(id,       other.id)	&& Objects.equals(pw,     other.pw)    && Objects.equals(name,     other.name)
			&& Objects.equals(gender, other.gender)	&& Objects.equals(email,    other.email)
			&& Objects.equals(phone,  other.phone) 	&& Objects.equals(birthday, other.birthday)
			&& Objects.equals(businessNum,  other.businessNum);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id,    pw,      name, gender,
							email, phone, birthday, businessNum);
	}
}