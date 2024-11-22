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
public class MemberDTO {
	
	/** 회원 아이디 */
	private String id;
	
	/** 회원 패쓰워드 */
	private String pw;
	
	/** 회원 이름 */
	private String name;
	
	/** 회원 성별 */
	private String gender;
	
	/** 회원 이메일 */
	private String email;
	
	/** 회원 연락처(휴대폰) */
	private String phone;
	
	/** 회원 우편번호 */
	private String zip;
	
	/** 회원 도로명 주소 */
	private String roadAddress;
	
	/** 회원 지번 주소 */
	private String jibunAddress;
	
	/** 회원 상세 주소 */
	private String detailAddress;
	
	/** 회원 생일 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date birthday;
	
	/** 회원 가입일 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date joindate;
	
	/** 회원 종류*/
	private String numberType;
	
	/** 사업자 번호*/
	private String businessNum;
	
}