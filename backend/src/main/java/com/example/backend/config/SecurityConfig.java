package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // CSRF 보호 비활성화
            .authorizeHttpRequests(authz ->
                authz
                    .requestMatchers("/login", "/register", "/css/**", "/js/**", "/login.html").permitAll() // 로그인 페이지 및 정적 리소스 허용
                    .anyRequest().authenticated() // 나머지 요청은 인증 필요
            )
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/login") // 프론트엔드 로그인 페이지로 리디렉션 (React 라우팅에서 처리)
                    .permitAll() // 모든 사용자에게 로그인 페이지 허용
            )
            .logout(logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout") // 로그아웃 후 리디렉션
                    .permitAll()
            );

        return http.build();
    }

    // CORS 설정
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000"); // React 프론트엔드 주소
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

