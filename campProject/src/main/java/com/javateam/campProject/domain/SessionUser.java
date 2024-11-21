package com.javateam.campProject.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// session에서 회원 정보를 저장하기 위한 클래스
@ToString
@Getter
@Setter
public class SessionUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String email;
    private String gender;
    private String birthday;

    private String mobile;
    private String memberType;
    private String authVendor;

    // SocialUser 객체를 통해 SessionUser 객체를 생성하는 생성자
    public SessionUser(SocialUser socialUserVO) {
        this.id       = socialUserVO.getId();
        this.name     = socialUserVO.getName();
        this.email    = socialUserVO.getEmail();
        this.gender   = socialUserVO.getGender();
        this.birthday = socialUserVO.getBirthday();

        this.mobile     = socialUserVO.getMobile();
        this.memberType = socialUserVO.getMemberType();
        this.authVendor = socialUserVO.getAuthVendor();
    }
}
