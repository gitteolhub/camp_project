package com.javateam.campProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.javateam.campProject.domain.CampImgJoinVO;

public interface CampJoinRepository extends CrudRepository<CampImgJoinVO, Integer>{

   @Query(value = "SELECT INFO.C_NO, IMG.IMG_KIND, IMG.IMG_NAME, INFO.CAMP_NAME "
				 + "FROM CAMP_INFO_TBL INFO, CAMP_IMG_TBL IMG "
				 + "WHERE INFO.C_NO = IMG.C_NO "
				 + "AND INFO.C_NO = :cNo", nativeQuery=true)
   List<CampImgJoinVO> findImgAndCampNameByCno(@Param("cNo") int cNo);

}