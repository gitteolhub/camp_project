package com.javateam.campProject.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javateam.campProject.domain.CampReservationVO;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CampReservationDAOImpl implements CampReservationDAO{

	@Autowired
	public SqlSession sqlSession;

	private static final String MAPPER_PATH="mapper.campReservationMapper.";

	public boolean insertReservation(CampReservationVO objCampReservationVO) {

		boolean blRetVal = false;

		try {
			sqlSession.insert(MAPPER_PATH + "insertReservation", objCampReservationVO);
			blRetVal = true;

		} catch (Exception ex) {
			log.error("[CampReservationDAOImpl][insertReservation] Exception : " + ex);
		}
		return blRetVal;
	}
}
