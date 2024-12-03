package com.javateam.campProject.domain;

import java.sql.Date;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@ToString
@Getter
@Setter
public class SocialUser {

	private int id;
    private String name;
    private String email;
    private String gender;
    private String birthday;

    private String mobile;
    private String memberType; // 사업자, 회원(기정값)
    private Date createdDate; // 생성일
    private Date modifiedDate; // 수정일
    private String authVendor;

    private SocialRole role;
//    private String role;

    @Override
	public boolean equals(Object object) {

		// 같은 객체인지 비교
		if (this == object)
			return true;

		// 비교 대상이 null인지 확인
		if (object == null)
			return false;

		// 클래스 타입이 같은지 확인
		if (getClass() != object.getClass())
			return false;

		SocialUser other = (SocialUser) object;
		return Objects.equals(id, other.id)                && Objects.equals(name, other.name)
		    && Objects.equals(email, other.email)          && Objects.equals(gender, other.gender)
		    && Objects.equals(birthday, other.birthday)    && Objects.equals(mobile, other.mobile)
		    && Objects.equals(memberType, other.memberType)&& Objects.equals(authVendor, other.authVendor)
		    && Objects.equals(role, other.role);
    }

    @Override
    public int hashCode() {
    	return Objects.hash(id,     name,       email,      gender, birthday,
    						mobile, memberType, authVendor, role);
    }

 // 회원 정보를 업데이트하는 메서드
 	public SocialUser update(String strName, String strGender, String strBirthday, String strMobile, String strMemberType,
 							 String strAuthVendor) {
 		this.name       = strName;
 		this.gender     = strGender;
 		this.birthday   = strBirthday;
 		this.mobile     = strMobile;
 		this.memberType = strMemberType;
 		this.authVendor = strAuthVendor;

 		return this;
 	}

 	// 역할 키를 반환하는 메서드
 	public String getRoleKey() {
 		return this.role.getKey();
 	}
}
