package com.javateam.campProject.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
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

	// 개인 예약정보 조회
	public CampReservationVO selectReservation(CampReservationVO objCampReservationVO) {

		return sqlSession.selectOne(MAPPER_PATH + "selectReservation", objCampReservationVO);
	}

	// 개인 예약정보 리스트 조회
	@Override
	public List<CampReservationVO> selectReservationList(CampReservationVO objCampReservationVO) {

		return sqlSession.selectList(MAPPER_PATH + "selectReservationList", objCampReservationVO);
	}

	@Override
	public void deleteReservation(int id) {
		sqlSession.delete(MAPPER_PATH + "deleteReservation", id);
	}

	@Override
	public List<CampReservationVO> selectAllReservationList() {
		return sqlSession.selectList(MAPPER_PATH + "selectAllReservationList");
	}

	@Override
	public void updateReservation(int id, int availSite) {
		Map<String, Integer> map = new HashMap<>();

		map.put("id", id);
		map.put("availSite", availSite);

		sqlSession.delete(MAPPER_PATH + "updateReservation", map);
	}
}
