package com.example.demo.config;

import com.example.demo.dto.MemberLoginDto;
import com.example.demo.model.JwtAuthenticationResponse;
import com.example.demo.model.MemberVO;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    public JwtAuthenticationResponse signin(MemberLoginDto memDto) {
        MemberVO rmemVO = memberService.getMemberEmail(memDto);
        long id = rmemVO.getId();
        String username = rmemVO.getUsername();
        String email = rmemVO.getEmail();
        String role = rmemVO.getRole();
        String accessToken = jwtTokenProvider.createToken(email, role);
        String refreshToken = jwtTokenProvider.createRefreshToken(email, role);
        log.info(refreshToken);
        JwtAuthenticationResponse jaResponse = new JwtAuthenticationResponse();
        jaResponse.setRefreshToken(refreshToken);
        jaResponse.setAccessToken(accessToken);
        jaResponse.setRole(role);
        jaResponse.setEmail(email);
        jaResponse.setId(id);
        jaResponse.setUsername(username);
        return jaResponse;
    }//end of signin
    public JwtAuthenticationResponse refreshToken(String token) {
        String email = jwtTokenProvider.extractEmail(token);
        MemberLoginDto memberLoginDto = new MemberLoginDto();
        memberLoginDto.setEmail(email);
        MemberVO rmemVO = memberService.getMemberEmail(memberLoginDto);
        if(jwtTokenProvider.isTokenValid(token, rmemVO)) {
            String accessToken = jwtTokenProvider.createToken(rmemVO.getEmail(), rmemVO.getRole());
            String refreshToken = jwtTokenProvider.createRefreshToken(rmemVO.getEmail(), rmemVO.getRole());
            JwtAuthenticationResponse jaResponse = new JwtAuthenticationResponse();
            jaResponse.setRefreshToken(refreshToken);
            jaResponse.setAccessToken(accessToken);
            return  jaResponse;
        }
        return null;
    }
}
