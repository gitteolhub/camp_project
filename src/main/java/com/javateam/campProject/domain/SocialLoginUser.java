package com.javateam.campProject.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*현재 로그인한 소셜 회원정보를 주입하기 위해 사용*/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface SocialLoginUser {

}
