package com.example.backend.config;

import com.example.backend.security.JwtAuthenticationFilter;
import com.example.backend.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // CSRF 비활성화
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // CORS 설정
            .authorizeHttpRequests(requests -> requests  // 권한 설정
                .requestMatchers("/login", "/register", "/css/**", "/js/**", "/api/auth/**").permitAll()  // 로그인 및 정적 파일 허용
                .requestMatchers("/").permitAll()  // 홈 페이지는 인증 없이 접근 허용
                .requestMatchers("/api/subjects/**").authenticated()  // /api/subjects 경로는 인증 필요
                .anyRequest().authenticated()  // 나머지 경로는 인증 필요
            )
            .formLogin(form -> form
                .loginPage("/login")  // 로그인 페이지 URL 설정
                .permitAll()  // 로그인 페이지는 모든 사용자에게 허용
                .defaultSuccessUrl("/home", true)  // 로그인 성공 후 리디렉션할 페이지 설정
            )
            .logout(logout -> logout.permitAll())  // 로그아웃 경로 허용
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))  // 인증되지 않은 접근 처리
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 세션 생성 정책 설정
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);  // JWT 필터 추가

        return http.build();
    }

    // CORS 설정
    private UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);  // 크로스 도메인 인증 정보 허용
        config.addAllowedOriginPattern("*");  // 모든 출처 허용
        config.addAllowedHeader("*");  // 모든 헤더 허용
        config.addAllowedMethod("*");  // 모든 HTTP 메서드 허용
        config.addAllowedHeader("Authorization");  // Authorization 헤더 허용
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
