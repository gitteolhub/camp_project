package com.javateam.repsitory;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.campProject.domain.MemberVO;
import com.javateam.campProject.repository.MemberDAO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class MemberJoinTest {

	@Autowired
	MemberDAO memberDAO;
	
	MemberVO memberVO;
	
	@BeforeEach
	public void setUp() {
		memberVO = MemberVO.builder()
				   .id("javajava1234")
				   .pw("#Abcd1234")
				   .name("홍길동")
				   .gender("m")
				   .email("javajava1234@abcd.com")
				   .mobile("010-7878-8989")
				   .phone("02-1111-2222")
				   .zip("08290")
				   .roadAddress("서울특별시 관악구 신림로 340")
				   .jibunAddress("서울특별시 관악구 신림동 1422-5 르네상스 복합쇼핑몰")
				   .detailAddress("6층 MBC 아카데미")
				   .birthday(Date.valueOf("2000-01-01"))
				   .build();
	}
	
	@Test
	void MemberJoinTest() {

		log.info("회원가입1");
		
		boolean result = memberDAO.insertMember(memberVO);
		
		assertTrue(result);
	}

}
