package com.javateam.campProject.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.javateam.campProject.domain.CampEntity;


@Mapper
public interface CampDAOMyBatis {

	// https://mybatis.org/mybatis-3/ko/dynamic-sql.html#script
	@Select({"<script>",
		     "SELECT * FROM camp_info_tbl ",
			 "WHERE ${regionColumn} IN ",
			 "		<foreach item='region' index='index' collection='regionList' ",
			 "			     open='(' separator=',' close=')' nullable='true'>",
			 "			#{region} ",
			 "		</foreach> ",
			 "		<if test='searchColumn != null'>",
			 "			AND ${searchColumn} = #{searchColumnVal}",
			 "		</if> ",
			 "		<if test='searchWord != null'>",
			 "			AND (road_address like '%${searchWord}%' or jibun_address like '%${searchWord}%')",
			 "		</if>",
			 "</script>"})
	public List<CampEntity> getCampBySearching(@Param("regionColumn") String regionColumn,
											   @Param("regionList") List<String> regionList,
											   @Param("searchColumn") String searchColumn,
											   @Param("searchColumnVal") String searchColumnVal,
											   @Param("searchWord") String searchWord);

	// 추가 : 캠핑장별 C_NO(고캠핑 캠핑장 고유 아이디) 필드값 업데이트
	@Update("UPDATE CAMP_INFO_TBL SET C_NO = #{cNo} WHERE CAMP_NAME = #{campName}")
	public void updateCampNoByCampName(@Param("campName") String campName, @Param("cNo") int cNo);

	// 추가 : 캠핑장별 C_NO(고캠핑 캠핑장 고유 아이디) 필드값 업데이트
	@Update("UPDATE CAMP_INFO_TBL SET C_NO = #{cNo} WHERE ID = #{id}")
	public void updateCampNoById(@Param("id") int id, @Param("cNo") int cNo);

	// 추가 : 캠핑장별 이미지 추가
	@Insert("INSERT INTO CAMP_IMG_TBL VALUES "
		  + "(CAMP_IMG_TBL_SEQ.NEXTVAL, #{cNo}, #{imgKind}, #{imgName})")
	public void insertCampImg(@Param("cNo") int cNo, @Param("imgKind") String imgKind, @Param("imgName") String imgName);

}