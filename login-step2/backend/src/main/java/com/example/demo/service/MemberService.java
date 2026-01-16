package com.example.demo.service;

import com.example.demo.dao.MemberDao;
import com.example.demo.dto.MemberLoginDto;
import com.example.demo.model.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberDao memberDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public MemberVO getMemberDetail(String socialId) {
        MemberVO rmVO = memberDao.getMemberDetail(socialId);
        return rmVO;
    }
    public MemberVO getMemberEmail(MemberLoginDto memDto) {
        MemberVO rmVO = memberDao.getMemberEmail(memDto);
        return rmVO;
    }//end of getMemberEmail
    public MemberVO oauthCreate(String socialId, String username, String password
                              , String email, String socialType) {
        MemberVO pmVO = MemberVO.builder()
                .username(username)
                .password(password)
                .email(email)
                .socialId(socialId)
                .socialType(socialType)
                .role("ROLE_USER")
                .build();
        memberDao.memberInsert(pmVO);
        return pmVO;
    }

    public int memberInsert(MemberVO memberVO) {
        log.info("memberInsert");
        log.info("memberVO:{}", memberVO);
        int result = -1;
        memberVO.setPassword(bCryptPasswordEncoder.encode(memberVO.getPassword()));
        result = memberDao.memberInsert(memberVO);
        return result;
    }

    public MemberVO login(MemberLoginDto memDto) {
        log.info("login");
        log.info("memDto:{}", memDto);
        MemberVO rmemVO = null;
        //TODO email을 조건검색해서 조회결과를 가져온다.
        //사용자가 입력한 비번과 DB에서 꺼낸 비번을 같은지 비교해야 한다.
        // BCryptPasswordEncoder가 제공하는 matches():boolean메서드의 파라미터로
        // 사용자가 입력한 비번과 DB에서 꺼내온 비번을 비교하고 판정을 해줌
        rmemVO = memberDao.getMemberEmail(memDto);
        return rmemVO;
    }
}
