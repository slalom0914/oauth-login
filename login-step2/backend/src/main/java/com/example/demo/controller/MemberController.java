package com.example.demo.controller;

import com.example.demo.config.JwtTokenProvider;
import com.example.demo.dto.GoogleProfileDto;
import com.example.demo.dto.MemberLoginDto;
import com.example.demo.dto.RedirectDto;
import com.example.demo.model.AccessTokenVO;
import com.example.demo.model.MemberVO;
import com.example.demo.service.GoogleService;
import com.example.demo.service.KakaoService;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final JwtTokenProvider jwtTokenProvider;
    // GoogleService의존성 주입
    private final GoogleService googleService;//주의:null초기화 하지 않음
    private final KakaoService kakaoService;
    private final MemberService memberService;

    // http://localhost:8000/member/memberInsert,
    @PostMapping("/memberInsert")
    public ResponseEntity<?> memberInsert(@RequestBody MemberVO memberVO) {
        int result = -1;
        result = memberService.memberInsert(memberVO);
        //return new ResponseEntity<>(result, HttpStatus.CREATED);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }//end of insertMember
    // front와 backend가 서로 이종간이다.
    // backend에서는 세션을 사용할 수 있지만 front에서는 세션을 사용불가
    // http://localhost:8000/member/doLogin
    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody MemberLoginDto memDto) {//email, password
        MemberVO memberVO = memberService.login(memDto);
        log.info("memberVO:{}", memberVO);
        String jwtToken = null;//TODO - 토큰 프로바이더 추가
        jwtToken = jwtTokenProvider.createToken(memberVO.getEmail(), memberVO.getRole());
        log.info("jwtToken:{}", jwtToken);
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", memberVO.getId());
        loginInfo.put("token", jwtToken);
        loginInfo.put("role", memberVO.getRole());
        loginInfo.put("email", memberVO.getEmail());
        loginInfo.put("username", memberVO.getUsername());
        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }//end of doLogin


    // http://localhost:8000/member/google/doLogin, {code: '12345678'}
    // 파라미터로 사용되는 @RequestBody은 리액트가 전송하는 객체 리터럴을 받아줌
    @PostMapping("/google/doLogin")
    public ResponseEntity<?> googleLogin(@RequestBody RedirectDto redirectDto){
        log.info("googleLogin");
        // 1.프론트에서 넘어온 인가 코드(code)를 받는 API
        // React가 구글 로그인 성공 후, 구글이 준 code를 백엔드로 전달함
        // 이 code는 'Access Token발급'을 위한 1회성 교환권 같은 값임
        log.info("redirectDto:{}",redirectDto.getCode());
        // 2. 인가코드로 구글 Access Token 발급 받기
        AccessTokenVO accessTokenVO = googleService.getAccessToken(redirectDto.getCode());
        log.info("구글서버가 보내준 AccessToken:{}",accessTokenVO.getAccess_token());
        // access token은 구글 API를 호출할 수 있는 열쇠
        // 3. Access Token으로 구글 사용자 프로필 정보 가져오기
        GoogleProfileDto googleProfileDto =
                googleService.getGoogleProfile(accessTokenVO.getAccess_token());
        // 4. 회원가입이 되어 있는지 여부를 파악해서 강제로 회원가입을 시킨다.
        MemberVO mVO = memberService.getMemberDetail(googleProfileDto.getSub());
        if(mVO==null){
            mVO = memberService.oauthCreate(googleProfileDto.getSub()
                                          , googleProfileDto.getName()
                                          , "123"
                                          , googleProfileDto.getEmail(), "GOOGLE");
        }
        else{
            log.info("이미 회원가입이 되어있는 socialId 입니다."+mVO.getSocialId());
        }
        // 5. 우리 서비스에서 사용할 JWT토큰 발급하기

        // 6. 프론트로 내려줄 로그인 결과 구성
        Map<String,Object> loginInfo = new HashMap<>();
        loginInfo.put("token",accessTokenVO.getAccess_token());
        loginInfo.put("email", googleProfileDto.getEmail());
        loginInfo.put("name", googleProfileDto.getName());
        return new ResponseEntity<>(loginInfo, HttpStatus.OK);//200번
    }//end of doLogin
    // http://localhost:8000/member/kakao/doLogin
    @PostMapping("/kakao/doLogin")
    public ResponseEntity<?> kakaoLogin(@RequestBody RedirectDto redirectDto){
        log.info("doLogin");
        log.info("redirectDto:{}",redirectDto.getCode());
        AccessTokenVO accessTokenVO = kakaoService.getAccessToken(redirectDto.getCode());
        return new ResponseEntity<>(redirectDto, HttpStatus.OK);
    }//end of doLogin
}
