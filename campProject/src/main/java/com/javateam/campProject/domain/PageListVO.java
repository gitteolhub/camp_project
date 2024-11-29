package com.javateam.campProject.domain;

import java.util.List;

import lombok.Data;

@Data
public class PageListVO {
	
	private List<CampVO> campList;
	
	private PageVO pageVO;
	

}
