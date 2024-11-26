package com.javateam.campProject.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FileDeleteMapper {

	@Delete("DELETE FROM INQUIRY_BOARD_UPFILE WHERE ID = #{id}")
	public void deleteFile(@Param("id") int id);

}