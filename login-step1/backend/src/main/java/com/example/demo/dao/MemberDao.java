package com.example.demo.dao;

import com.example.demo.model.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@RequiredArgsConstructor
public class MemberDao {
    private final SqlSessionTemplate sqlSessionTemplate;
    public MemberVO getMemberDetail(String socialId) {
        log.info("socialId:"+socialId);
        //member.xml에 등록된 select문의 아이디값
        MemberVO rmVO = sqlSessionTemplate.selectOne("getMemberDetail", socialId);
        return rmVO;
    }//end of getMemberDetail

    public void memberInsert(MemberVO pmVO) {
        log.info("memberInsert:"+pmVO);
        int result = -1;
        result = sqlSessionTemplate.insert("memberInsert", pmVO);
        log.info("result:"+result);//1이면 입력 성공, 0이면 입력 실패
    }//end of memberInsert
}
