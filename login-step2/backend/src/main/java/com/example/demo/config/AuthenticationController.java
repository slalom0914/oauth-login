package com.example.demo.config;

import com.example.demo.dto.MemberLoginDto;
import com.example.demo.model.JwtAuthenticationResponse;
import com.example.demo.model.RefreshTokenRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    //로그인 요청 처리 - 이메일과 비번을 사용자로 부터 입력 받아서 처리한다.
    @PostMapping("/signin")
    public ResponseEntity<Map<String,Object>> signin(@RequestBody MemberLoginDto memDto) {
        log.info("signin:"+memDto);
        //서비스계층에서 signin메서드 호출하기
        JwtAuthenticationResponse jaResponse = authenticationService.signin(memDto);
        Map<String,Object> loginInfo = new HashMap<>();
        loginInfo.put("id", jaResponse.getId());
        loginInfo.put("accessToken",jaResponse.getAccessToken());
        loginInfo.put("refreshToken",jaResponse.getRefreshToken());
        loginInfo.put("username", jaResponse.getUsername());
        loginInfo.put("email", jaResponse.getEmail());
        return new ResponseEntity<>(loginInfo,HttpStatus.OK);
    }//end of signin
    // React로 부터 refreshToken을 받아와서 검증한다.
    @PostMapping("/refresh")
    public ResponseEntity<Map<String,Object>> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        log.info("refresh:"+refreshTokenRequest);
        JwtAuthenticationResponse jaResponse = authenticationService.refreshToken(refreshTokenRequest.getRefreshToken());
        Map<String,Object> loginInfo = new HashMap<>();
        loginInfo.put("id", jaResponse.getId());
        loginInfo.put("accessToken",jaResponse.getAccessToken());
        loginInfo.put("refreshToken",jaResponse.getRefreshToken());
        loginInfo.put("username", jaResponse.getUsername());
        loginInfo.put("email", jaResponse.getEmail());
        return new ResponseEntity<>(loginInfo,HttpStatus.OK);
    }//end of refresh
}
