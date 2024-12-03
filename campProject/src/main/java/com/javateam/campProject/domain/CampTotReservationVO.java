package com.javateam.campProject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "CAMP_TOT_RESERVATION_TBL")
@Data
public class CampTotReservationVO {

	@Id
	@Column(name = "CAMP_ID")
	private int campId;

	@Column(name = "SITE")
	private int site;

	@Column(name = "AVAIL_SITE")
	private int availSite;

}
