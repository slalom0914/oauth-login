package com.example.demo.config;

import jakarta.servlet.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
/*
JwtTokenFilter의 역할은 클라이언트가 요청을 할 때 토큰을 달고 다님
이 토큰이 정상적인 것인지 서버측에서 검증하는 과정이 필요한데 이것을 여기서 처리함
즉 토큰을 검증하는 코드를 작성해야 함.
 */
@Component
public class JwtTokenFilter extends GenericFilter {
    @Value("${jwt.secret}")
    private String secretKey;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

    }
}
