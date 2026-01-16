package com.example.demo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String token = httpRequest.getHeader("Authorization");
        try{
            //token이 null이라는 건 토큰을 넣지 않았다는 것임
            if (token != null) {
                //Bearer를 붙이는게 컨벤션이다.
                if(!token.substring(0, 7).equals("Bearer ")) {
                    throw new AuthenticationServiceException("Bearer 형식이 아닙니다.");
                }
                //검증을 할 때는 Bearer를 떼어내고 검증함.
                String jwtToken = token.substring(7);
                //이 토큰을 가지고 검증하고 여기서 claims는 payload를 가리키는데
                //이것을 꺼내서 Authentication이라는 인증 객체를 만들 때 사용.
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(jwtToken)
                        .getBody();//검증을 하고 Claims를 꺼내는 메서드임
                List<GrantedAuthority> authorities = new ArrayList<>();//권한이 여러가지 일 수 있으므로 List에 담아줌.
                authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
                //claims.getSubject()에 Email정보가 들어있음
                UserDetails userDetails = new User(claims.getSubject(), "", authorities);
                //스프링에서는 Authentication객체가 있으면 로그인을 했다라고 판단함
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, jwtToken, userDetails.getAuthorities());
                //인증정보는 SecurityContextHolder안에 SecurityContext안에 들어있다.
                //log.info(SecurityContextHolder.getContext().getAuthentication().getName());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            //아래 코드가 없으면 다음 필터로 연결이 안됨.
            //필터를 갔다가 다시 FilterChain으로 돌아가게 하는 코드임
            //토큰에 대한 확인이 되었으니 다시 원래 프로세스로 돌아간다.
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());//401응답줌
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("invalid token");
        }

    }
}
