package com.javateam.campProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.javateam.campProject.domain.CampEntity;


public interface CampRepository extends CrudRepository<CampEntity, Integer> {

	List<CampEntity> findByLatitudeAndLongitude(String latitude, String longitude);

	List<CampEntity> findByRoadAddress(String roadAddress);

	@Query(value = "select * from camp_info_tbl "
			     + "where latitude >= :latitude1 and latitude <= :latitude2 "
			     + "and longitude >= :longitude1 and  longitude <= :longitude2", nativeQuery=true)
	List<CampEntity> findByLatitudeBetweenAndLongitudeBetween(@Param("latitude1") double latitude1,
															  @Param("latitude2") double latitude2,
															  @Param("longitude1") double logitude1,
															  @Param("longitude2") double logitude2);

}
