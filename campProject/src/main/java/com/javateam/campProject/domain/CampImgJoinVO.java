package com.javateam.campProject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CampImgJoinVO {

	// 고캠핑 캠핑장 고유번호
	@Id
	@Column(name = "C_NO")
	private int cNo;

	// 이미지 종류 : 메인(M), 갤러리(G), 서브(S)
	@Column(name = "IMG_KIND")
	private String imgKind;

	// 이미지 파일명
	@Column(name = "IMG_NAME")
	private String imgName;

	// 캠프이름
	@Column(name = "CAMP_NAME")
	private String campName;

}