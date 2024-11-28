package com.javateam.campProject.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private static final String MAPPER_PATH = "mapper.campUserMapper.";

	// 주어진 키워드로로 캠프 이름 조회
	@Override
	public List<CampVO> selectCampingName(String campName) {

		return sqlSession.selectList(MAPPER_PATH + "selectCampingName", campName);
	}

	// 주어진 키워드로로 캠프 이름 조회
	@Override
	public List<CampVO> selectCate3Name(String cate3) {

		return sqlSession.selectList(MAPPER_PATH + "selectCate3Name", cate3);
	}

	@Override
	public int countCate3Name(String cate3) {

		return sqlSession.selectOne(MAPPER_PATH + "countCate3Name", cate3);
	}

	@Override
	public List<CampVO> selectCate3NameByPaging(String searchName, int page, int limit) {
		Map<String, Object> map = new HashMap<>();
		map.put("searchName", searchName);
		map.put("page", page);
		map.put("limit", limit);
		
		return sqlSession.selectList(MAPPER_PATH + "selectCate3NameByPaging", map);
	}
}
