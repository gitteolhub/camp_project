package com.javateam.campProject.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="inquiry_board_upfile")
public class BoardUploadFile {
	
	@Id
    // @GeneratedValue
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    			    generator = "INQULRY_BOARD_UPFILE_SEQ_GENERATOR")
	@SequenceGenerator(
    	    name = "INQULRY_BOARD_UPFILE_SEQ_GENERATOR",
    	    sequenceName = "inqulry_board_upfile_seq",
    	    initialValue = 1,
    	    allocationSize = 1)
	@Column(name = "id")
    int id;

    @Column(name = "filename")
    String fileName;
    
    @Column(name="save_filename")
    String saveFileName;
    
    @Column(name="file_path")
    String filePath;
    
    @Column(name="content_type")
    String contentType;
    
    @Column(name="file_size")
    long fileSize;
    
    @Column(name="regdate")
    Date regDate;

}
