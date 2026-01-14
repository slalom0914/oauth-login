package com.example.demo.service;

import com.example.demo.dto.GoogleProfileDto;
import com.example.demo.model.AccessTokenVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Log4j2
@Service
public class GoogleService {
    @Value("${oauth.google.client-id}")
    private String clientId;
    @Value("${oauth.google.client-secret}")
    private String clientSecret;
    @Value("${oauth.google.redirect-uri}")
    private String redirectUri;
    // 구글에서 발급하는 accessToken 받기
    // 구글계정으로 로그인을 하면 인가코드를 보내줌
    // code는 구글서버에서 보내준 인가코드이다.
    // 리액트에서 보내준 인가코드를 가지고 구글서버에 AccessToken을 요청함.
    public AccessTokenVO getAccessToken(String code) {
        log.info(code);//인가코드
        // 서버(8000번 스프링)에서 서버(구글 서버)로 요청을 할 때는
        // RestClient로 처리함(spring 6)
        // 리액트에서 axios가 하는 역할과 비슷
        RestClient restClient = RestClient.create();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        params.add("code", code);//인가코드 보내야 함
        ResponseEntity<AccessTokenVO> res = restClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(params)
                .retrieve()
                .toEntity(AccessTokenVO.class);
        log.info(res.getBody());
        return res.getBody();
    }//end of getAccessToken
    // 사용자 정보 얻기
    public GoogleProfileDto getGoogleProfile(String token) {
        log.info("getGoogleProfile");
        log.info("token:{}",token);
        //스프링서버에서 구글 서버로 요청하기
        RestClient restClient = RestClient.create();
        ResponseEntity<GoogleProfileDto> response = restClient.get()
                .uri("https://openidconnect.googleapis.com/v1/userinfo")
                .header("Authorization", "Bearer "+token)
                .retrieve()
                .toEntity(GoogleProfileDto.class);
        return response.getBody();
    }//end of getGoogleProfile
}
