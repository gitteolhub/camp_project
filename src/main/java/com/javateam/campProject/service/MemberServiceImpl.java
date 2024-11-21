package com.javateam.campProject.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.javateam.campProject.domain.MemberVO;
import com.javateam.campProject.exception.UserNotFoundException;
import com.javateam.campProject.repository.MemberDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberDAO memberDAO;
	
	@Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

	// 아이디로 회원 정보를 조회
	@Override
	public MemberVO selectMemberById(MemberVO objMemberVO) {
		return memberDAO.selectMemberById(objMemberVO);
	}

	@Override
	public String findUserIdByNameEmail(String strName, String strEmail) {
		try {
			Map<String, String> params = new HashMap<>();
	        params.put("name", strName);
	        params.put("email", strEmail);
	        String strRetVal = memberDAO.findUserIdByNameEmail(params);
	        log.info("[MemberService][findUserIdByNameEmail]");

	        if (strRetVal == null) {
                throw new UserNotFoundException("[해당하는 사용자 ID를 찾을 수 없습니다]");
            }
	        return strRetVal;

		} catch (Exception ex) {
			log.error("[MemberService][findUserIdByNameEmail]Exception: {}",ex);
			throw new RuntimeException("[아이디 찾기 중 오류가 발생했습니다]: {}", ex);
		}

	}

	@Override
	public String findUserPwByID(String id) {
		try {
			String strRetVal =  memberDAO.findUserPwByID(id);

			if(strRetVal == null) {
				throw new UserNotFoundException("해당 ID에 대한 비밀번호를 찾을 수 없습니다.");
			}
			return strRetVal;
		} catch (Exception ex) {
			throw new RuntimeException("[비밀번호 찾기 중 오류가 발생했습니다]: {}", ex);
		}
	}

	@Override
	public String findUserEmailByID(String id) {
		try {
			String strRetVal =  memberDAO.findUserEmailByID(id);

			if(strRetVal == null) {
				throw new UserNotFoundException("해당 ID에 대한 이메일을 찾을 수 없습니다.");
			}
			return strRetVal;
		} catch (Exception ex) {
			throw new RuntimeException("[이메일 조회 중 오류가 발생했습니다]: {}", ex);
		}
	}

	// 이메일로 회원 Id 조회
	@Override
	public String findUserIdByEmail(String strEmail) {
		try {
			String strRetVal =  memberDAO.findUserIdByEmail(strEmail);

			if(strRetVal == null) {
				throw new UserNotFoundException("해당 이메일에 대한 ID를 찾을 수 없습니다.");
			}
			return strRetVal;
		} catch (Exception ex) {
			throw new RuntimeException("[ID 조회 중 오류가 발생했습니다]: {}", ex);
		}
	}

	// 비밀번호 업데이트
	@Override
	public boolean updatePw(String strId, String strPw) {
		boolean blRetVal = false;
		
		try {
			// 비밀번호 암호화
			String encodedPassword = bCryptPasswordEncoder.encode(strPw);
			memberDAO.updatePw(strId, encodedPassword);
			blRetVal = true;
		} catch (Exception ex) {
			log.error("[MemberService][updatePw]: {}", ex);
		}
		
		return blRetVal;
	}
}
