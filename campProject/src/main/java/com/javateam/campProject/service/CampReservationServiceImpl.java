package com.javateam.campProject.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.campProject.domain.CampEntity;
import com.javateam.campProject.domain.CampReservationVO;
import com.javateam.campProject.domain.CampTotReservationVO;
import com.javateam.campProject.domain.MemberVO;
import com.javateam.campProject.domain.SocialUser;
import com.javateam.campProject.repository.CampDAOMyBatis;
import com.javateam.campProject.repository.CampReservationDAO;
import com.javateam.campProject.repository.CampSiteRepository;
import com.javateam.campProject.repository.CampTotReservationRepository;
import com.javateam.campProject.repository.MemberDAO;
import com.javateam.campProject.repository.SocialUserDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CampReservationServiceImpl implements CampReservationService{

	@Autowired
	CampReservationDAO campReservationDAO;

	@Autowired
	SocialUserDAO socialUserDAO;

	@Autowired
	MemberDAO memberDAO;

	@Autowired
	CampDAOMyBatis campDAOMyBatis;

	@Autowired
	CampSiteRepository campSiteRepository;

	@Autowired
	CampTotReservationRepository campTotReservationRepository;

	@Transactional
	@Override
	public boolean insertReservation(CampReservationVO objCampReservationVO, String memberId, String socialUserId, int campCNO,
									 Date checkInDate, Date checkOutDate) {
		log.info("[CampReservationService][insertReservation]");
		log.info("[CampReservationService][memberId(null 여부)]: {}", memberId==null);
		log.info("[CampReservationService][memberId(공백문자열 여부)]: {}", memberId.trim().equals(""));

		boolean blRetVal = false;

		// 캠핑 예약시 예약 테이블에 정보 추가
		try {

			if(memberId.trim().equals("") == false) {
				log.info("[자체로그인]");
				MemberVO member = memberDAO.memberReservationInfo(memberId);
				log.info("[CampReservationService][member]: {}", member);

				objCampReservationVO.setUserName(member.getName());
				objCampReservationVO.setUserEmail(member.getEmail());
				objCampReservationVO.setUserPhone(member.getPhone());

			} else {
				log.info("[소셜로그인]");

				String[] arr = socialUserId.split("_");
				String email = arr[0];
				String authVendor = arr[1];
				SocialUser socialUser = socialUserDAO.socialReservationInfo(email, authVendor);
				log.info("[CampReservationService][socialUser]: {}", socialUser);

				objCampReservationVO.setUserName(socialUser.getName());
				objCampReservationVO.setUserEmail(socialUser.getEmail());
				objCampReservationVO.setUserPhone(socialUser.getMobile());
			}
			CampEntity campEntity = campDAOMyBatis.campReservationInfo(campCNO);
			log.info("[CampReservationService][campEntity]: {}", campEntity);

			if(campEntity != null) {
				objCampReservationVO.setCampCNo(campEntity.getCNo());
				objCampReservationVO.setCampName(campEntity.getCampName());
			}

			// 체크인, 체크아웃 날짜
			objCampReservationVO.setCheckIn(checkInDate);
			objCampReservationVO.setCheckOut(checkOutDate);

			campReservationDAO.insertReservation(objCampReservationVO);
			blRetVal = true;

		} catch (Exception ex) {
			log.error("[CampReservationService][insertReservation][Exception]: {}", ex);
		}

		return blRetVal;
	}

	// 개인 예약정보 조회
	@Override
	public CampReservationVO selectReservation(CampReservationVO objCampReservationVO) {

		return campReservationDAO.selectReservation(objCampReservationVO);
	}

	// 캠핑장 사이트수 조회 후 예약가능 여부 점검
	@Transactional(noRollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	@Override
	public boolean isEnableReservation(CampReservationVO objCampReservationVO) {
		boolean blRetVal = false;

		// 3-1 예약할 캠핑장 사이트수 조회
        // 3-2 해당 캠핑장에 예약 가능 사이트를 조회 후 비교
		CampTotReservationVO campTotReservationVO
				= campTotReservationRepository.findById(objCampReservationVO.getCampId()).get();
		int availSite = campTotReservationVO.getAvailSite();
		int limitSite = campTotReservationVO.getSite();

		log.info("[CampReservationService][availSite]: {}", availSite);
		log.info("[CampReservationService][limitSite]: {}", limitSite);

		if (availSite >= limitSite) {
			blRetVal = false; // 예약 불가
		} else {
			availSite++;
			campTotReservationVO.setAvailSite(availSite);
			campTotReservationRepository.save(campTotReservationVO);
			blRetVal = true; // 예약 가능
		}

		return blRetVal;
	}

	// 개인 예약정보 리스트 조회
	@Override
	public List<CampReservationVO> selectReservationList(CampReservationVO objCampReservationVO) {

		return campReservationDAO.selectReservationList(objCampReservationVO);
	}



}
