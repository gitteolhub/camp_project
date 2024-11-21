package com.javateam.campProject.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.javateam.campProject.domain.OAuthAttributes;
import com.javateam.campProject.domain.SessionUser;
import com.javateam.campProject.domain.SocialUser;
import com.javateam.campProject.repository.SocialUserDAO;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Getter
@Setter
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final HttpSession httpSession;

	@Autowired
	private SocialUserDAO socialUserDAO;

	@SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

        log.info("[oAuth2UserRequest]:{}", oAuth2UserRequest);

        // 기본 OAuth2 사용자 정보를 가져오는 서비스
        OAuth2UserService delegate   = new DefaultOAuth2UserService();
        OAuth2User        oAuth2User = delegate.loadUser(oAuth2UserRequest);

        // 클라이언트 등록 ID와 사용자 속성 이름을 가져옴(OAuth 인증 정보 추출)
        String registrationId        = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration().getProviderDetails()
                										.getUserInfoEndpoint().getUserNameAttributeName();

        log.info("[oAuth2User.getAttributes()]: {}", oAuth2User.getAttributes());

        // OAuthAttributes 객체를 생성하여 사용자 정보를 매핑
        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        log.info("[oAuthAttributes]:{}", oAuthAttributes);
        log.info("[CustomOAuth2UserService][socialUserDAO]:{}", socialUserDAO);

        // 사용자 정보를 저장하거나 업데이트
        SocialUser socialUser = saveOrUpdate(oAuthAttributes);
        socialUser.setAuthVendor(oAuthAttributes.getAuthVendor()); // 인증 제공자 설정

        log.info("[CustomOAuth2UserService][loadUser][socialUser]: {}", socialUser);

        // 세션에 socialUser 정보 저장
        // social(구글 & 네이버) 인증 session
        httpSession.setAttribute("socialUser", new SessionUser(socialUser));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(socialUser.getRoleKey())), // 사용자 권한
                oAuthAttributes.getAttributes(), // 사용자 속성
                oAuthAttributes.getNameAttributeKey()); // 사용자 이름 속성
    	}

	// 사용자 정보를 저장하거나 업데이트하는 메서드
	private SocialUser saveOrUpdate(OAuthAttributes oAuthAttributes) {

        log.info("[saveOrUpdate],[oAuthAttributes]:{}", oAuthAttributes);

        SocialUser socialUser;

        // naver의 경우
        if (oAuthAttributes.getAuthVendor().equals("naver")) {
            // Optional을 사용하여 이메일로 사용자 조회
            Optional<SocialUser> optionalUser = Optional.ofNullable(socialUserDAO.selectSocialMemberByEmail(oAuthAttributes.getEmail()));

            // 회원 정보가 존재하는 경우
            if (optionalUser.isPresent()) {
            	log.info("[CustomOAuth2UserService][네이버 회원정보 있을때]");

                // 사용자 정보 업데이트
                socialUser = optionalUser.get().update(oAuthAttributes.getName(),
                        							   oAuthAttributes.getGender(),
                        							   oAuthAttributes.getBirthday(),
                        							   oAuthAttributes.getMobile(),
                        							   oAuthAttributes.getMemberType(),
                        							   oAuthAttributes.getAuthVendor());
            } else {
            	log.info("[CustomOAuth2UserService][네이버 회원정보 없을때]");

                // 새로운 회원 객체 생성
                socialUser = oAuthAttributes.toEntity();

                log.info("[CustomOAuth2UserService][socialUser]: {}", socialUser);

                // Member_Type 기정값 "회원"
                socialUser.setMemberType("회원");

                // 새로운 회원정보 저장
                socialUserDAO.insertSocialUser(socialUser);
            }
        } else { // google의 경우
            log.info("[google Email]: {}", oAuthAttributes.getEmail());

            Optional<SocialUser> optionalUser = Optional.ofNullable(socialUserDAO.selectSocialMemberByEmail(oAuthAttributes.getEmail()));

            // 회원정보가 없을 경우
            if (optionalUser.isEmpty()) {
                log.info("[CustomOAuth2UserService][구글 회원정보 없을때]");

                // 새로운 회원 객체 생성
                socialUser = oAuthAttributes.toEntity();

                // gender 기정값 "없음" >> 로그인 시 입력하도록
                socialUser.setGender("없음");

                // birthday 기정값 "없음" >> 로그인 시 입력하도록
                socialUser.setBirthday("없음");

                // birthday 기정값 "없음" >> 로그인 시 입력하도록
                socialUser.setMobile("없음");

                // Member_Type 기정값 "회원"
                socialUser.setMemberType("회원");

                // 새로운 회원정보 저장
                socialUserDAO.insertSocialUser(socialUser);
            } else {
                // 회원정보가 있을 경우
                log.info("[CustomOAuth2UserService][구글 회원정보 있을때]");
                socialUser = optionalUser.get();
            }

            log.info("[socialUser]: {}", socialUser);
        }

        return socialUser;
	}

}
