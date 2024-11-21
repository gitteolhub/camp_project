package com.javateam.campProject.service;

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
			log.error("[MemberService][updateSocialUser]: {}", ex);
				ex.printStackTrace();
		}

		return blRetVal;
	}

}


