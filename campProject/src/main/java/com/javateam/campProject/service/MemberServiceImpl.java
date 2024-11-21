package com.javateam.campProject.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.javateam.campProject.domain.MemberJsonVO;
import com.javateam.campProject.domain.MemberVO;
import com.javateam.campProject.domain.Role;
import com.javateam.campProject.repository.MemberDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

	@Autowired
	public PlatformTransactionManager dataSourceTransactionManager;
	public TransactionTemplate        transactionTemplate;

	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	void setTransactionTemplate (PlatformTransactionManager transactionManager) {

		this.transactionTemplate = new TransactionTemplate(transactionManager);
	}

	@Autowired
	MemberDAO memberDAO;

	// 아이디로 회원 정보를 조회
	@Transactional (readOnly = true)
	@Override
	public MemberVO selectMemberById(String strId) {

		return memberDAO.selectMemberById(strId);
	}

	// 중복 아이디 확인후 새로운 회원 추가
	@Override
	public boolean insertMember(MemberVO objMemberVO) {

		return transactionTemplate.execute(new TransactionCallback<Boolean>(){

			@Override
			public Boolean doInTransaction(TransactionStatus objTransactionstatus) {

				boolean blRetVal = false;

				try {
					log.info("기존 회원 존재여부: {}", memberDAO.hasMemberByFld("ID", objMemberVO.getId()));

					// 중복 아이디가 존재하면 예외 발생
					if (memberDAO.hasMemberByFld("ID",objMemberVO.getId()) == true) {
						throw new Exception("중복되는 아이디가 존재합니다.");
					}
					log.info("[MemberService][insertMember]: {}", objMemberVO);

					 // 비밀번호 암호화
                    String encodedPassword = passwordEncoder.encode(objMemberVO.getPw());
                    objMemberVO.setPw(encodedPassword); // 암호화된 비밀번호를 설정
					
					// 회원 정보 저장
					blRetVal = memberDAO.insertMember(objMemberVO);

				} catch (Exception ex) {
	 				log.error("[MemberService][insertMember] Exception: {}", ex);
					objTransactionstatus.setRollbackOnly();
				}

				if(blRetVal == true) {

				// 회원 Role 생성
				try {
					if (memberDAO.hasMemberByFld("ID", objMemberVO.getId()) == false) {
						throw new Exception ("회원정보가 존재하지 않습니다.");
					}
					log.info("[MemberService] Role 생성: {}", objMemberVO);

					// 가입된 회원 정보 조회
					MemberVO memberVO = memberDAO.selectMemberById(objMemberVO.getId());
					
					String role = memberVO.getMemberType().equals("user") ? "ROLE_USER" : 
								  memberVO.getMemberType().equals("ceo") ? "ROLE_CEO" : "ROLE_ADMIN";
									  
					blRetVal = memberDAO.insertRole(memberVO.getId(), role);

				} catch (Exception ex) {
					log.error("[MemberService][insertMember](Role) Exception: {}", ex);
					objTransactionstatus.setRollbackOnly();
				}

				}
				return blRetVal;
			}
		});
	}


	// 회원정보 중복 점검(회원 가입)
	@Transactional(readOnly = true)
	@Override
	public boolean hasMemberByFld(String strField, String strValue) {

		return memberDAO.hasMemberByFld(strField, strValue);

	}

	// 회원 role 생성
	@Transactional
	@Override
	public boolean insertRole(Role role) {

		boolean blRetVal = false;

		try {
			memberDAO.insertRole(role.getUsername(), role.getRole());
			blRetVal = true;
		} catch (Exception ex) {
			log.error("[MemberService][insertRole]: {}", ex);
			ex.printStackTrace();
		}
		return blRetVal;
	}
}