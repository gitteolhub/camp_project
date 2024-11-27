package com.javateam.campProject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="CAMP_SITE_TBL")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CampSiteVO {

	@Id
	@Column(name="C_NO")
	private int cNo;

	// 사이트 수 ex) 숙박 야영장 사이트 갯수
	@Column(name="SITE_CONTENT")
	private String siteContent;

	// 사이트 수 ex) 숙박 야영장 사이트 갯수
	@Column
	private int site;

	// 반려동물 동반가능 여부
	@Column(name="PET_YN")
	private String petYn;

}
