package com.javateam.campProject.service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javateam.campProject.domain.CampEntity;
import com.javateam.campProject.domain.CampReservationVO;
import com.javateam.campProject.domain.MemberVO;
import com.javateam.campProject.domain.SocialUser;
import com.javateam.campProject.repository.CampDAOMyBatis;
import com.javateam.campProject.repository.CampReservationDAO;
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

	@Override
	public boolean insertReservation(CampReservationVO objCampReservationVO, String memberId, String socialUserId, int campCNO,
									 Date checkInDate, Date checkOutDate) {
		log.info("[CampReservationService][insertReservation]");

		boolean blRetVal = false;

		// 캠핑 예약시 예약 테이블에 정보 추가
		try {

			if(memberId != null) {
				MemberVO member = memberDAO.memberReservationInfo(memberId);
				log.info("[CampReservationService][member]: {}", member);

				objCampReservationVO.setUserName(member.getName());
				objCampReservationVO.setUserEmail(member.getEmail());
				objCampReservationVO.setUserPhone(member.getPhone());

			} else {
				SocialUser socialUser = socialUserDAO.socialReservationInfo(socialUserId);
				log.info("[CampReservationService][socialUser]: {}", socialUser);

				objCampReservationVO.setUserName(socialUser.getName());
				objCampReservationVO.setUserEmail(socialUser.getEmail());
				objCampReservationVO.setUserPhone(socialUser.getMobile());
			}
			CampEntity campEntity = campDAOMyBatis.campReservationInfo(campCNO);
			log.info("[CampReservationService][campEntity]: {}", campEntity);

//			log.info("[CampReservationService][member]: {}", member);
//			log.info("[CampReservationService][socialUser]: {}", socialUser);

//			if(member != null) {
//				objCampReservationVO.setUserName(member.getName());
//				objCampReservationVO.setUserEmail(member.getEmail());
//				objCampReservationVO.setUserPhone(member.getPhone());
//			} else if (socialUser != null) {
//				objCampReservationVO.setUserName(socialUser.getName());
//				objCampReservationVO.setUserEmail(socialUser.getEmail());
//				objCampReservationVO.setUserPhone(socialUser.getMobile());
//			}


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

	// 체크인, 체크아웃 날짜 변환(String)
//	@Override
//	public Date convertStringToDate(String dateString) {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
//        try {
//        	java.util.Date utilDate = formatter.parse(dateString);
//            return new Date(utilDate.getTime());
//        } catch (ParseException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }

}
