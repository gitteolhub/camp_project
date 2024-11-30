package com.javateam.campProject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="CAMP_SITE_TBL")
@Data
public class CampSiteVO {

	@Id
	@Column(name="C_NO")
	private int cNo;

	@Column(name="SITE_CONTENT")
	private String siteContent;

	@Column(name="SITE")
	private String site;

	@Column(name="PET_YN")
	private String petYn;
}
