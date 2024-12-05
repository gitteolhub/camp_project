package com.javateam.campProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.campProject.domain.SocialUser;
import com.javateam.campProject.repository.SocialUserDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SocialUserServiceImpl implements SocialUserService{

	@Autowired
	SocialUserDAO socialUserDAO;

	// social (google) 회원정보 수정
	@Transactional
	@Override
	public boolean updateSocialGoogleUser(SocialUser socialUser) {
		boolean blRetVal = false;

		try {
			socialUserDAO.updateSocialGoogleUser(socialUser);
			blRetVal = true;
		} catch (Exception ex) {
			log.error("[SocialUserServiceImpl][updateSocialUser]: {}", ex);
				ex.printStackTrace();
		}

		return blRetVal;
	}

	// social 회원 조회
	@Override
	public SocialUser selectSocialUser(String email, String authVendor) {
		return socialUserDAO.selectSocialMemberByEmailAndAuthVendor(email, authVendor);
	}

	@Override
	public List<SocialUser> selectSocialAllMembers() {
		return socialUserDAO.selectSocialAllMembers();
	}

	@Override
	public SocialUser selectSocialUserById(int id) {
		return socialUserDAO.selectSocialMemberById(id);
	}

	@Override
	public void updateRole(int id, String role) {
		socialUserDAO.updateRole(id, role);
	}

	@Override
	public List<SocialUser> selectSocialAllMembersByRole(String role) {
		return socialUserDAO.selectSocialAllMembersByRole(role);
	}

	@Override
	public boolean deleteSocialUser(int id) {
		boolean blRetVal = false;

		try {
			socialUserDAO.deleteSocialUser(id);
			blRetVal = true;
		} catch (Exception ex) {
			log.error("[SocialUserServiceImpl][deleteSocialUser]: {}", ex);
				ex.printStackTrace();
		}

		return blRetVal;
	}

}


