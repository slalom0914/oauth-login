package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(configurationSource()))
                .csrf(AbstractHttpConfigurer::disable) //csrf 비활성화(MVC에 많은 공격이 있음.)
                //Basic 인증 비활성화
                //Basic인증은 사용자 이름과 비밀번호를 Base64로 인코딩하여 인증값으로 활용
                //토큰 방식은 signature부분에 암호화가 들어가므로 Basic과는 다르다
                .httpBasic(AbstractHttpConfigurer::disable)
                //세션 방식을 비활성화
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //특정 url패턴에 대해서는 인증처리(Authentication객체 생성) 제외
                .authorizeHttpRequests(a-> a.requestMatchers("/member/memberInsert", "/member/doLogin"
                        , "/member/google/doLogin", "/member/kakao/doLogin").permitAll().anyRequest().authenticated())
                .build();
    }
    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("*"));//모든 HTTP메서드 허용
        configuration.setAllowedHeaders(Arrays.asList("*"));//모든 헤더값 허용
        configuration.setAllowCredentials(true);//자격증명 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //모든 url패턴에 대해서 cors 허용 설정
        //별 두개가 있으면 디렉토리까지 파고들어 간다.
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
