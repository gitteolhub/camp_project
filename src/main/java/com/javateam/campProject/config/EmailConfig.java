package com.javateam.campProject.config;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

//@Configuration
//@Slf4j
public class EmailConfig {
//	@Value("{spring.mail.username}")
//	String username;
//
//	@Value("{spring.mail.password}")
//	String password;
//
//	@Value("{spring.mail.port}")
//	int port;

	// https://docs.spring.io/spring-boot/docs/3.2.9/reference/htmlsingle/#io.email
	// https://docs.spring.io/spring-framework/reference/6.1/integration/email.html

	// @Bean
	public JavaMailSender getJavaMailSender() {

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.naver.com");
		mailSender.setPort(587);
		// mailSender.setPort(port);

		mailSender.setUsername("사용자 계정 아이디");
		mailSender.setPassword("사용자 계정 패쓰워드");
//		mailSender.setUsername(username);
//		mailSender.setPassword(password);

		Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");

		return mailSender;
	} //

}

