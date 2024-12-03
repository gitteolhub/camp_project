package com.javateam.campProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.javateam.campProject.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;

	// 비밀번호를 안전하게 암호화하기 위해 BCryptPasswordEncoder 빈 생성
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity objHttpSecurity) throws Exception {

		objHttpSecurity.headers(headersCustomizer -> headersCustomizer
					   .frameOptions(Customizer.withDefaults()).disable());

		// 요청 권한 설정
		// 마이페이지는 권한 정확히 설정된 후 권한이 필요한 경로로 이동
		objHttpSecurity.authorizeHttpRequests(authorizeHttpRequests ->
	    authorizeHttpRequests
	        .requestMatchers("/",                   "/resources/**",           "/loginError",       "/loginForm",            "/home",
	        				 "/captcha",            "/checkCaptcha",           "/captcha/image/**", "/refreshImage",         "/socialAddInformation",
	        				 "/findUserId",         "/findUserPw",             "/findUserIdProc",   "/pwVerificationCode",   "/findUserPwProc",
	        				 "/findUserPwResult",   "/choiceJoin",             "/userJoin",         "/ceoJoin",              "/member/joinProc2",
	        				 "/myPageModification", "/myPageReservationCheck", "/userManagement",   "/campGroundManagement", "/adminManagement",
	        				 "/search",             "/searchCamp",             "/searchCate3Name",   "/recomCamp" ,          "/recomCampProc",
	        				 "/searchRecomCamp",    "/campDetail"
	        )
	        .permitAll()
	        .requestMatchers("/admin/**")
	        .hasAnyAuthority("ROLE_ADMIN") // ROLE_ADMIN 권한이 필요한 경로
	        .requestMatchers("/myPage", "/campReserve") // TODO: 추후 변경
	        .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN", "ROLE_CEO", "ROLE_SUPERADMIN")	// ROLE_USER, ROLE_ADMIN 권한이 필요한 경로
			// 게시판 관련 링크 추가
			.requestMatchers("/board/inquiryBoardWrite","/board/inquiryBoardWriteProc",
							 "/board/image", "/board/image/**", "/board/inquiryBoardList",
							 "/board/inquiryBoardView", "/board/searchList", "/board/inquiryBoardView_no_pw",
							 "/board/inquiryBoardUpdate", "/board/inquiryBoardUpdateProc",
							 "/board/replyWrite", "/board/replyUpdate", "/board/replyUpdateNoPw",
							 "/board/getRepliesAll", "/board/replyDelete",
							 "/board/deleteProc").authenticated()
	        .anyRequest()
	        .authenticated()
	);


		// csrf 토큰 미사용
		objHttpSecurity.csrf((csrf) -> csrf.disable());

		// 로그인/ 로그아웃(인증) 처리
		objHttpSecurity.formLogin(formLogin -> formLogin
						   	.loginProcessingUrl("/loginForm")
						   	.loginPage("/loginForm")		// 로그인 이후 주소
						   	.usernameParameter("userId")	// 아이디
						   	.passwordParameter("password")	// 비밀번호
						   	.defaultSuccessUrl("/myPageModification")	// 로그인 성공시 이동 주소
						   	.failureUrl("/loginError")		// 로그인 에러 처리
						   	.permitAll())

					   .logout((logout) -> logout
							.logoutSuccessUrl("/loginForm")	// 로그아웃 이후 이동 주소
							.permitAll());

		objHttpSecurity.oauth2Login(oauth2LoginCustomizer -> oauth2LoginCustomizer
					   .defaultSuccessUrl("/socialAddInformation")
		//			   .loginProcessingUrl("/login")
				   	   .loginPage("/loginForm")
				   	   .failureUrl("/loginForm")
					   .userInfoEndpoint(userInfoEndpointCustomizer -> userInfoEndpointCustomizer
								   .userService(customOAuth2UserService)));

		// 예외처리 이용 주소
		objHttpSecurity.exceptionHandling(handler -> handler.accessDeniedPage("/403"));


		return objHttpSecurity.build();
	}

	// security URL 열외(제외)
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {

		return (web) -> web.ignoring().requestMatchers("/bootstrap/**",        "/css/**",     "/js/**",         "/axios/**",     "/webjars/**",
				   									   "/social_login_img/**", "/swagger-ui", "/swagger-ui/**", "/v3/api-docs",  "/bootstrap-icons/**",
				   									   "/summernote/**",       "/jquery/**",  "/images/**",     "/resources/**", "/img/**",
				   									   "/campImg/**",          "/campImgPath/**");

	}
}
