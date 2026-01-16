package com.example.demo.config;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/*
JWT 토큰을 발급하는 스프링 컴포넌트(싱글톤)
이메일과 권한(ROLE_) 정보를 안전하게 암호화해서 토큰으로 반환
이 토큰으로 사용자의 인증/인가 처리
토큰은 만료시간이 있어, 보안성을 높이고, 세션리스서비스 구현에 적합
 */
@Component
public class JwtTokenProvider {
    //인코딩된 시크릿값
    private final String secretKey;//yaml등록되어 있어
    private final int expiration;
    private Key SECRET_KEY;
    public JwtTokenProvider(@Value("${jwt.secret}") String  secretKey, @Value("${jwt.expiration}") int expiration) {
        this.secretKey = secretKey;
        this.expiration = expiration;
        this.SECRET_KEY = new SecretKeySpec(Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS512.getJcaName());
    }

}
