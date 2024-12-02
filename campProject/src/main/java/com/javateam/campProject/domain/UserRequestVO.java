package com.javateam.campProject.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

//@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserRequestVO {

	/** 요구사항 아이디 */
	// @Id
	//@Column
	private int id;

	/** 사용자 아이디 */
	//@Column(name="user_id")
	private String userId;

	/** 행선예정지 ex) 강릉 */
	//@Column(name="destination")
	private String destination;

	/** 행선 시기 ex) 2024년 11월 2일 → 가을 */
	private LocalDate time;

	/* 동행자 수 */
//	@Column(name="number_of_people")
//	private int numberOfPeople;

	/** 전기 사용가 여부 */
	//@Column(name="is_electricity")
	private int isElectricity;

	/** WIFI 인터넷 사용가 여부 */
	//@Column(name="is_wifi")
	private int isWifi;

	/** 캠프파이어 가능 여부 */
	//@Column(name="is_campfire")
	private int isCampfire;

	/** 온수 가능 여부 */
	//@Column(name="is_heater")
	private int isHeater;

	/** 풀장 가능 여부 */
	//@Column(name="is_pool")
	private int isPool;

	/** 놀이터 가능 여부 */
	//@Column(name="is_playground")
	private int isPlayground;

	/** 취사 시설 구비 여부 */
	//@Column(name="is_sink")
	private int isSink;

	/** 산책로 사용 가능 여부 */
	//@Column(name="is_trail")
	private int isTrail;

	/** 수상 레저 가능 여부 */
	//@Column(name="is_maritime_leisure")
	private int isMaritimeLeisure;

	/** 낚시 가능 여부 */
	//@Column(name="is_fishing")
	private int isFishing;

}