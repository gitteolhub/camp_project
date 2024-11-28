package com.javateam.campProject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="CAMP_INFO_TBL")
@SequenceGenerator(
	    name = "CAMP_INFO_TBL_SEQ_GENERATOR",
	    sequenceName = "CAMP_INFO_TBL_SEQ",
	    initialValue = 1,
	    allocationSize = 1)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampEntity {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "CAMP_INFO_TBL_SEQ_GENERATOR")
	private int id;

	@Column(name="CAMP_NAME")
	private String campName;

	@Column(name="CATE1")
	private String cate1;

	@Column(name="CATE2")
	private String cate2;

	@Column(name="CATE3")
	private String cate3;

	@Column(name="SIDO_NAME")
	private String sidoName;

	@Column(name="SIGUGUN_NAME")
	private String sigugunName;

	@Column(name="EUPMYUNDONG_NAME")
	private String eupmyundongName;

	@Column(name="RY_NAME")
	private String ryName;

	@Column(name="BUNJI_NAME")
	private String bunjiName;

	@Column(name="ROAD_NAME")
	private String roadName;

	@Column(name="BUILDING_NUM")
	private String buildingNum;

	@Column(name="LATITUDE")
	private String latitude;

	@Column(name="LONGITUDE")
	private String longitude;

	@Column(name="ZIP")
	private String zip;

	@Column(name="ROAD_ADDRESS")
	private String roadAddress;

	@Column(name="JIBUN_ADDRESS")
	private String jibunAddress;

	@Column(name="PHONE")
	private String phone;

	@Column(name="HOMEPAGE")
	private String homepage;

	@Column(name="VENDOR")
	private String vendor;

	@Column(name="WEEKDAY_OP_STATUS")
	private String weekdayOpStatus;

	@Column(name="WEEKEND_OP_STATUS")
	private String weekendOpStatus;

	@Column(name="SPRING_OP_STATUS")
	private String springOpStatus;

	@Column(name="SUMMER_OP_STATUS")
	private String summerOpStatus;

	@Column(name="fall_op_status")
	private String fallOpStatus;

	@Column(name="winter_op_status")
	private String winterOpStatus;

	@Column(name="facil_electricity")
	private String facilElectricity;

	@Column(name="facil_hot_water")
	private String facilHotWater;

	@Column(name="facil_wifi")
	private String facilWifi;

	@Column(name="facil_campfire")
	private String facilCampfire;

	@Column(name="facil_trail")
	private String facilTrail;

	@Column(name="facil_pool")
	private String facilPool;

	@Column(name="facil_playground")
	private String facilPlayground;

	@Column(name="facil_mart")
	private String facilMart;

	@Column(name="facil_restroom")
	private String facilRestroom;

	@Column(name="facil_showerroom")
	private String facilShowerroom;

	@Column(name="facil_sink")
	private String facilSink;

	@Column(name="facil_extinguisher")
	private String facilExtinguisher;

	@Column(name="surr_facil_fishing")
	private String surrFacilFishing;

	@Column(name="surr_facil_trail")
	private String surrFacilTrail;

	@Column(name="surr_facil_beach")
	private String surrFacilBeach;

	@Column(name="surr_facil_maritime_leisure")
	private String surrFacilMaritimeLeisure;

	@Column(name="surr_facil_valley")
	private String surrFacilValley;

	@Column(name="surr_facil_stream")
	private String surrFacilStream;

	@Column(name="surr_facil_pool")
	private String surrFacilPool;

	@Column(name="surr_facil_youth_experience")
	private String surrFacilYouthExperience;

	@Column(name="surr_facil_rural_experience")
	private String surrFacilRuralExperience;

	@Column(name="surr_facil_childrens_play")
	private String surrFacilChildrensPlay;

	@Column(name="glam_bed")
	private String glamBed;

	@Column(name="glam_tv")
	private String glamTv;

	@Column(name="glam_freezer")
	private String glamFreezer;

	@Column(name="glam_internet")
	private String glamInternet;

	@Column(name="glam_restroom")
	private String glamRestroom;

	@Column(name="glam_aircon")
	private String glamAircon;

	@Column(name="glam_heater")
	private String glamHeater;

	@Column(name="glam_cookware")
	private String glamCookware;

	@Column(name="facil_characteristics")
	private String facilCharacteristics;

	@Column(name="facil_detail")
	private String facilDetail;

	@Column(name="reg_date")
	private String regDate;

	// 추가 : content_no : gocamping 캠핑장 고유 아이디
	@Column(name="c_no")
	private int cNo;

}
