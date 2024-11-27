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
@Table(name="camp_review_tbl2")
@Data
@SequenceGenerator(
	    name = "CAMP_REVIEW_TBL_SEQ_GENERATOR",
	    sequenceName = "CAMP_REVIEW_TBL_SEQ",
	    initialValue = 1,
	    allocationSize = 1)
public class CampReviewVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "CAMP_REVIEW_TBL_SEQ_GENERATOR")
	@Column(name="id")
	private long id;

	@Column(name="camp_name")
	private String campName;

	@Column(name="avg_rating")
	private String avgRating;

	@Column(name="reviews")
	private String reviews;

	@Column(name="predict")
	private int predict;

	public String[] toArray(CampReviewVO campReviewVO) {

		String[] arr = new String[3];
		arr[0] = campReviewVO.getCampName();
		arr[1] = campReviewVO.getAvgRating();
		arr[2] = campReviewVO.getReviews();

		return arr;
	} //

}