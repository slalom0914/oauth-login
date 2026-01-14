package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberVO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role = "RULE_USER";
    private String socialType; //GOOGLE, KAKAO, NAVER
    private String socialId; //uid, 제공업체가 제공하는 아이디
}
