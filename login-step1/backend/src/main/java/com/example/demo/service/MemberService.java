package com.example.demo.service;

import com.example.demo.dao.MemberDao;
import com.example.demo.model.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberDao memberDao;
    public MemberVO getMemberDetail(String socialId) {
        MemberVO rmVO = memberDao.getMemberDetail(socialId);
        return rmVO;
    }

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
}
