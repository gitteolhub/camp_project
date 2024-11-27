package com.javateam.campProject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="CAMP_IMG_TBL")
@Data
@SequenceGenerator(
	    name = "CAMP_IMG_TBL_SEQ_GENERATOR",
	    sequenceName = "CAMP_IMG_TBL_SEQ",
	    initialValue = 1,
	    allocationSize = 1)
public class CampImgVO {

	// 아이디
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "CAMP_IMG_TBL_SEQ_GENERATOR")
	@Column(name="ID")
	private int id;

	// 고캠핑 캠핑장 고유번호
	@Column(name="C_NO")
	private int cNo;

	// 이미지 종류 : 메인(M), 갤러리(G), 서브(S)
	@Column(name="IMG_KIND")
	private String imgKind;

	// 이미지 파일명
	@Column(name="IMG_NAME")
	private String imgName;

}