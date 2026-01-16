package com.example.demo.config;

import com.example.demo.model.MemberVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

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
    }//end of JwtTokenProvider
    /*
    Claims생성
    JWT의 payload 부분(=실제 데이터)에 들어갈 내용
    setSubject(email): 이 토큰의 주인은 email(주체정보)
    claims.put("role", role): 사용자 권한(로우 하이어라키)
    토큰 생성
    setIssuedAt(now): 토큰 발급 시간
    setExpiration(): 토큰 만료 시간(현재시간+유효시간)
    signWith(SECRET_KEY):앞서 만든 Key로 HS512해시 알고리즘으로 서명
    토큰 반환
    .compact(): JWT문자열 직렬화해서 반환
    이 토큰을 프론트엔드(리액트)에게 응답하면, 프론트는 이 토큰(access token)을 저장하고
    API요청할 때 마다 Authorization헤더에 넣어 인증
    */
    public String createToken(String email, String role) {
        //Claims는 jwt토큰의 payload부분을 의미함.
        //각종 사용자 정보를 payload에 넣을 수 있다.
        Claims claims = Jwts.claims().setSubject(email);//주된 정보는 이메일로함
        claims.put("role", role);
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) //발행시간
                //getTime()은 밀리세크 단위이다.
                .setExpiration(new Date(now.getTime() + expiration*60*1000L))
                .signWith(SECRET_KEY)
                .compact();
        return token;
    }//end of createToken
    /*
    AccessToken 재발급에 사용(유효기간이 더 길다 - 여기서는 7일)
     */
    public String createRefreshToken(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email);//주된 정보는 이메일로함
        claims.put("role", role);
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) //발행시간
                //getTime()은 밀리세크 단위이다.
                //만료 시간을 7일로 설정하기
                .setExpiration(new Date(now.getTime() + expiration*1000L*60*60*24*7))
                .signWith(SECRET_KEY)
                .compact();
        return token;
    }//end of createToken
    //subject를 email로 쓴다.
    public String extractEmail(String token) {
        //Claims::getSubject - 람다식
        return extractClaim(token, Claims::getSubject);
    }//end of extractEmail
    //토큰에서 특정 값을 꺼내는 공용 메서드
    //<T> : 이 메서드는 T라는 타입을 사용한다.
    //아직 타입을 정하지 않은 반환타입을 T로 놓음.
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers){
        final Claims claims = extractAllClaims(token);//서명 검증 + payload
        return claimsResolvers.apply(claims);
    }//end of extractClaim

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
    public boolean isTokenValid(String token, MemberVO pmemVO) {
        final String email =  extractEmail(token);
        return (email.equals(pmemVO.getEmail()) && !isTokenExpired(token));
    }
}
