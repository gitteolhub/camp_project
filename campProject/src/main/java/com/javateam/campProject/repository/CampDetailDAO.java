package com.javateam.campProject.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.javateam.campProject.domain.CampDTO;

@Mapper
public interface CampDetailDAO {

	@Select({"<script>",
			  "SELECT ",
			  "INFO.C_NO, ",
			  "INFO.ID, ",
			  "INFO.CAMP_NAME, ",
			  "INFO.CATE1, ",
			  "INFO.CATE2, ",
			  "INFO.CATE3, ",
			  "INFO.SIDO_NAME, ",
			  "INFO.SIGUGUN_NAME, ",
			  "INFO.EUPMYUNDONG_NAME, ",
			  "INFO.RY_NAME, ",
			  "INFO.BUNJI_NAME, ",
			  "INFO.ROAD_NAME, ",
			  "INFO.BUILDING_NUM, ",
			  "INFO.LATITUDE, ",
			  "INFO.LONGITUDE, ",
			  "INFO.ZIP, ",
			  "INFO.ROAD_ADDRESS, ",
			  "INFO.JIBUN_ADDRESS, ",
			  "INFO.PHONE, ",
			  "INFO.HOMEPAGE, ",
			  "INFO.VENDOR, ",
			  "INFO.WEEKDAY_OP_STATUS, ",
			  "INFO.WEEKEND_OP_STATUS, ",
			  "INFO.SPRING_OP_STATUS, ",
			  "INFO.SUMMER_OP_STATUS, ",
			  "INFO.FALL_OP_STATUS, ",
			  "INFO.WINTER_OP_STATUS, ",
			  "INFO.FACIL_ELECTRICITY, ",
			  "INFO.FACIL_HOT_WATER, ",
			  "INFO.FACIL_WIFI, ",
			  "INFO.FACIL_CAMPFIRE, ",
			  "INFO.FACIL_TRAIL, ",
			  "INFO.FACIL_POOL, ",
			  "INFO.FACIL_PLAYGROUND, ",
			  "INFO.FACIL_MART, ",
			  "INFO.FACIL_RESTROOM, ",
			  "INFO.FACIL_SHOWERROOM, ",
			  "INFO.FACIL_SINK, ",
			  "INFO.FACIL_EXTINGUISHER, ",
			  "INFO.SURR_FACIL_FISHING, ",
			  "INFO.SURR_FACIL_TRAIL, ",
			  "INFO.SURR_FACIL_BEACH, ",
			  "INFO.SURR_FACIL_MARITIME_LEISURE, ",
			  "INFO.SURR_FACIL_VALLEY, ",
			  "INFO.SURR_FACIL_STREAM, ",
			  "INFO.SURR_FACIL_POOL, ",
			  "INFO.SURR_FACIL_YOUTH_EXPERIENCE, ",
			  "INFO.SURR_FACIL_RURAL_EXPERIENCE, ",
			  "INFO.SURR_FACIL_CHILDRENS_PLAY, ",
			  "INFO.GLAM_AIRCON, ",
			  "INFO.GLAM_HEATER, ",
			  "INFO.GLAM_BED, ",
			  "INFO.GLAM_TV, ",
			  "INFO.GLAM_INTERNET, ",
			  "INFO.FACIL_CHARACTERISTICS, ",
			  "INFO.FACIL_DETAIL, ",
			  "IMG.IMG_KIND, ",
			  "IMG.IMG_NAME, ",
			  "SITE.SITE_CONTENT, ",
			  "SITE.SITE, ",
			  "SITE.PET_YN ",
			  "FROM CAMP_INFO_TBL INFO, CAMP_IMG_TBL IMG, CAMP_SITE_TBL SITE ",
			  "WHERE INFO.C_NO = IMG.C_NO ",
			  "  AND IMG.C_NO = SITE.C_NO ",
			  "  AND INFO.ID = #{campId}",
			  "</script>"
	})
	List<CampDTO> findCampDetailByCampId(@Param("campId")int campId);

}
