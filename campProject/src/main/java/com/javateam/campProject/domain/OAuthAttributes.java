package com.javateam.campProject.domain;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString
@Slf4j
public class OAuthAttributes {

	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;
	private String gender;

	private String birthday;
	private String mobile;
    private String memberType;
    private String authVendor;

    @Builder
	public OAuthAttributes(Map<String, Object> attribute,   String strNameAttributeKey, String strName,       String strEmail, String strGender,
									  String strBirthday,   String strMobile,           String strMemberType, String strAuthVendor) {
		this.attributes       = attribute;
		this.nameAttributeKey = strNameAttributeKey;
		this.name             = strName;
		this.email            = strEmail;
		this.gender           = strGender;

		this.birthday   = strBirthday;
		this.mobile     = strMobile;
		this.memberType = strMemberType;
		this.authVendor = strAuthVendor;	// 인증 제공자(ex: Naver, Google)
    }

    //  회원 정보를 생성하는 정적 메서드
	public static OAuthAttributes of(String strRegistrationId, String strUserNameAttributeName, Map<String, Object> attributes) {

		OAuthAttributes result = null;

		// 인증 제공자에 따른 처리
		if("naver".equals(strRegistrationId)) {

			log.info("[OAuthAttributes][naver]");
			result = ofNaver("id", attributes);

			log.info("[OAuthAttributes][result]:{}", result);

		} else if("google".equals(strRegistrationId)) {

			log.info("[OAuthAttributes][google]");
			result = ofGoogle(strUserNameAttributeName, attributes);
		}

		log.info("[OAuthAttributes][strRegistrationId]: {} ", strRegistrationId);

		result.setAuthVendor(strRegistrationId);

		return result;
	}

	// Google 회원 정보를 처리하는 메서드
	private static OAuthAttributes ofGoogle (String strUserNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
	  			  .strName((String) attributes.get("name"))
	  			  .strEmail((String) attributes.get("email"))
	  			  .attribute(attributes)
	  			  .strNameAttributeKey(strUserNameAttributeName)
	  			  .build();
	}

	// Naver 회원 정보를 처리하는 메서드
	@SuppressWarnings("unchecked")	// 경고 무시
	private static OAuthAttributes ofNaver(String strUserNameAttributeName, Map<String, Object> attributes) {

		// Naver 응답에서 회원 정보 추출
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");

		// 생년월일 추가 naver 생일 : birthyear + "-" + birthday   ex) 2000-01-01
		String birthDate = (String)response.get("birthyear") + "-" + (String)response.get("birthday");

		return OAuthAttributes.builder()
				  .strName((String) response.get("name"))
				  .strEmail((String) response.get("email"))
				  .strGender((String) response.get("gender"))
				  .strBirthday(birthDate)
				  .strMobile((String) response.get("mobile"))
				  .attribute(response)
				  .strNameAttributeKey(strUserNameAttributeName)
				  .build();
	}

	public SocialUser toEntity() {
		return SocialUser.builder()
						   .name(name)
						   .email(email)
						   .gender(gender)
						   .birthday(birthday)
						   .mobile(mobile)

						   .authVendor(authVendor)
						   .role(SocialRole.USER)
						  .build();
	}
}
