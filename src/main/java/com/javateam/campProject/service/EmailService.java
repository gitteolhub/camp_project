package com.javateam.campProject.service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {	// 텍스트 메일 전송 서비스
	
	@Autowired
	private JavaMailSender javaMailSender; // JavaMailSender를 자동 주입하여 이메일 전송 기능을 사용
	
	private static final String NUMBERS = "0123456789"; // 사용할 숫자(인증 번호)
	private static final int CODE_LENGTH = 6; // 인증번호 길이
	
	private Map<String, String> verificationCode = new HashMap<>(); // 이메일과 인증코드를 저장
	
	// 간단한 텍스트 이메일을 전송하는 메서드
	public void sendSimpleMessage(String from, String to, String subject, String text) {
		log.info("[EmailService][sendSimpleMessage]");
		
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom(from); 		  // 발신자 설정
        message.setTo(to); 			  // 수신자 설정
        message.setSubject(subject);  // 이메일 제목 설정
        message.setText(text);		  // 이메일 본문 텍스트 설정
        javaMailSender.send(message); // 이메일 전송
    }
	
	// MIME 형식의 이메일을 전송하는 메서드
	// https://docs.spring.io/spring-framework/reference/6.0/integration/email.html#mail-usage-mime
	public void sendMIMEMessage(String from, String to, String subject, String text, FileSystemResource file) {
		log.info("[EmailService][sendMIMEMessage]");
		
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage message) throws Exception {
				
				// MimeMessageHelper를 사용하여 MIME 메시지 설정
				final MimeMessageHelper mailHelper = new MimeMessageHelper(message, true, "UTF-8");
				 
				mailHelper.setFrom(from);
				mailHelper.setTo(to); 
				mailHelper.setSubject(subject); 
				mailHelper.setText(text, true); // 본문 텍스트 설정 (HTML 사용)/ html = true
				
				// 메일 첨부				
				mailHelper.addAttachment(file.getFilename(), file);
			} //
		
		};
		
		javaMailSender.send(preparator);
    } //
	
	public String sendRandomCode() {
		log.info("[EmailService][sendRandomCode]");
		
		SecureRandom secureRandom = new SecureRandom();
		StringBuilder code = new StringBuilder(CODE_LENGTH);
		
		for (int i = 0; i < CODE_LENGTH; i++) {
			int index = secureRandom.nextInt(NUMBERS.length());
			code.append(NUMBERS.charAt(index));	// 램던 숫자 추가
		}
		
		return code.toString(); 
	}
	
	// 이메일에 인증코드 저장
	public Map<String, String> saveCode(String email, String code) {
		log.info("[EmailService][saveCode]");
		
		verificationCode.put(email, code);
		return verificationCode;
	}
	
	// 인증코드 검증
	public boolean verityCode(String email, String code, HttpSession httpSession) {
		log.info("[EmailService][verityCode]");
		
		// 세션화된 인증코드 가져오기
		Map<String, String> map = (Map<String, String>)httpSession.getAttribute("sess_verificationCode");
		String verfiedCode = map.get(email);
		log.info("[verfiedCode]: {}",verfiedCode);
		
		// 세션회된 인증코드 삭제
		httpSession.removeAttribute("sess_verificationCode");
		
		// 코드 점검
		return code.equals(map.get(email));
	}

}
