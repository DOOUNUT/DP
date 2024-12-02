package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().and() // CORS 활성화
            .authorizeHttpRequests(authz -> 
                authz
                    .requestMatchers("/login", "/register", "/css/**", "/js/**", "/api/auth/**").permitAll() // API 경로 허용
                    .anyRequest().authenticated()
            )
            .formLogin(formLogin -> 
                formLogin
                    .loginPage("/login")
                    .permitAll()
            )
            .logout(logout -> 
                logout
                    .logoutUrl("/logout")
                    .permitAll()
            );

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // 모든 도메인 허용
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public StrictHttpFirewall strictHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true); // ';' 문자 허용 설정
        return firewall;
    }
}
