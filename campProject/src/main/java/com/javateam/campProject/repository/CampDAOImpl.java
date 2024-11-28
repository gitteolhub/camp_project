package com.javateam.campProject.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javateam.campProject.domain.CampVO;
import com.javateam.campProject.domain.MemberVO;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CampDAOImpl implements CampDAO {

	@Autowired
	public SqlSession sqlSession;

	private static final String MAPPER_PATH="mapper.campUserMapper.";

	// 주어진 키워드로로 캠프 이름 조회
		@Override
		public List<CampVO> selectCampingName(String campName) {

			return sqlSession.selectList(MAPPER_PATH + "selectCampingName", campName);
		}

}
