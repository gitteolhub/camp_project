package com.javateam.campProject.service;

import java.util.HashMap;
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

import com.javateam.campProject.domain.MemberVO;
import com.javateam.campProject.domain.Role;
import com.javateam.campProject.exception.UserNotFoundException;
import com.javateam.campProject.repository.MemberDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberDAO memberDAO;

	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public PlatformTransactionManager dataSourceTransactionManager;
	public TransactionTemplate        transactionTemplate;

	@Autowired
	void setTransactionTemplate (PlatformTransactionManager transactionManager) {

		this.transactionTemplate = new TransactionTemplate(transactionManager);
	}

	// 아이디로 회원 정보를 조회
	@Transactional (readOnly = true)
	@Override
	public MemberVO selectMemberById(String strId) {

		return memberDAO.selectMemberById(strId);
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
		String result = null;
		try {
			String strRetVal =  memberDAO.findUserEmailByID(id);

			if(strRetVal == null) {
//				throw new UserNotFoundException("해당 ID에 대한 이메일을 찾을 수 없습니다.");
				throw new Exception("해당 ID에 대한 이메일을 찾을 수 없습니다.");
			}
			result = strRetVal;
		} catch (Exception ex) {
			log.error("이메일 검색 에러: {}", ex);
//			throw new RuntimeException("[이메일 조회 중 오류가 발생했습니다]: {}", ex);
		}
		log.info("[MemberService][findUserEmailByID]");
		return result;
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
                    String encodedPassword = bCryptPasswordEncoder.encode(objMemberVO.getPw());
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

	@Override
	public boolean updateMember(MemberVO objMemberVO) {

		return transactionTemplate.execute(new TransactionCallback<Boolean>() {

			@Override
			public Boolean doInTransaction(TransactionStatus transactionStatus)	{
				boolean blRetVal = false;

				try {
					//  기존 회원 존재 여부
					if(memberDAO.hasMemberByFld("ID", objMemberVO.getId()) == false) {
						throw new Exception("수정할 회원정보가 존재하지 않습니다.");
					}

					// 수정한 비밀번호 암호화
                    String encodedPassword = bCryptPasswordEncoder.encode(objMemberVO.getPw());
                    objMemberVO.setPw(encodedPassword); // 암호화된 비밀번호를 설정

					blRetVal = memberDAO.updateMember(objMemberVO);

				} catch (Exception ex) {
					log.error("[MemberService][updateMember] Exception : " + ex);
					transactionStatus.setRollbackOnly();
				}

				return blRetVal;
			}
		});
	}

	@Override
	public List<Map<String, String>> selectAllUsersWithRole() {
		return memberDAO.selectAllUsersWithRole();
	}

	@Transactional
	@Override
	public void updateRole(String id, String role) {

		try {
			memberDAO.updateRole(id, role);
		} catch (Exception ex) {
			log.error("[MemberService][updateRole]: {}", ex);
			ex.printStackTrace();
		}
	}

	@Override
	public List<Map<String, String>> selectAllUsersByRole(String role) {
		return memberDAO.selectAllUsersByRole(role);
	}
	
	@Transactional
	@Override
	public boolean deleteMember(String id) {
		
		boolean blRetVal = false;
		try {
			memberDAO.deleteRole(id);
			blRetVal = true;
		} catch (Exception ex) {
			log.error("[MemberService][updateRole]: {}", ex);
			blRetVal = false;
			ex.printStackTrace();
		}
		
		try {
			memberDAO.deleteMember(id);
			blRetVal = true;
		} catch (Exception ex) {
			log.error("[MemberService][updateRole]: {}", ex);
			blRetVal = false;
			ex.printStackTrace();
		}
		
		return blRetVal;
	}
}