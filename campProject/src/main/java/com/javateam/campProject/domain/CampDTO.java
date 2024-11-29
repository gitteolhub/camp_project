package com.javateam.campProject.domain;

import java.util.List;

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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampDTO {

	@Id
	@Column private int id;

	@Column private String campName;

	@Column private String cate1;

	@Column private String cate2;

	@Column private String cate3;

	@Column private String sidoName;

	@Column private String sigugunName;

	@Column private String eupmyundongName;

	@Column private String ryName;

	@Column private String bunjiName;

	@Column private String roadName;

	@Column private String buildingNum;

	@Column private String latitude;

	@Column private String longitude;

	@Column private String zip;

	@Column private String roadAddress;

	@Column private String jibunAddress;

	@Column private String phone;

	@Column private String homepage;

	@Column private String vendor;

	@Column private String weekdayOpStatus;

	@Column private String weekendOpStatus;

	@Column private String springOpStatus;

	@Column private String summerOpStatus;

	@Column private String fallOpStatus;

	@Column private String winterOpStatus;

	@Column private String facilElectricity;

	@Column private String facilHotWater;

	@Column private String facilWifi;

	@Column private String facilCampfire;

	@Column private String facilTrail;

	@Column private String facilPool;

	@Column private String facilPlayground;

	@Column private String facilMart;

	@Column private String facilRestroom;

	@Column private String facilShowerroom;

	@Column private String facilSink;

	@Column private String facilExtinguisher;

	@Column private String surrFacilFishing;

	@Column private String surrFacilTrail;

	@Column private String surrFacilBeach;

	@Column private String surrFacilMaritimeLeisure;

	@Column private String surrFacilValley;

	@Column private String surrFacilStream;

	@Column private String surrFacilPool;

	@Column private String surrFacilYouthExperience;

	@Column private String surrFacilRuralExperience;

	@Column private String surrFacilChildrensPlay;

	@Column private String glamBed;

	@Column private String glamTv;

//	@Column private String glamFreezer;

	@Column private String glamInternet;

//	@Column private String glamRestroom;

	@Column private String glamAircon;

	@Column private String glamHeater;

//	@Column private String glamCookware;

	@Column private String facilCharacteristics;

	@Column private String facilDetail;

//	@Column private String regDate;

	@Column private int cNo;

	@Column private String imgKind;

	@Column private String imgName;

	@Column private String siteContent;

	// 사이트 수 ex) 숙박 야영장 사이트 갯수
	@Column private int site;

	// 반려동물 동반가능 여부
	@Column private String petYn;

}
