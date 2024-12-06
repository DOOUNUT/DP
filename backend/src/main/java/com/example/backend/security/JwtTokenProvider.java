package com.example.backend.security;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import com.example.backend.config.JwtConfig;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;
    private final JwtParser jwtParser;
    private final SecretKey secretKey;

    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;

        // JWT 비밀 키 길이를 보장 (512비트, 64바이트 이상)
        String secret = jwtConfig.getSecret();
        Objects.requireNonNull(secret, "JWT secret cannot be null");

        // 비밀키가 64바이트 미만이면 임의로 생성
        byte[] secretBytes;
        if (secret.length() < 64) {
            // 비밀키가 너무 짧으면 임의의 64바이트 키를 생성
            secretBytes = new byte[64];
            new SecureRandom().nextBytes(secretBytes);  // 64바이트 임의의 값 생성
        } else {
            secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        }

        this.secretKey = Keys.hmacShaKeyFor(secretBytes);

        // JwtParser 초기화
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();
    }

    // JWT 토큰 생성
    public String generateToken(String username, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration());

        // roles가 null일 경우 빈 리스트로 처리
        roles = roles == null ? List.of() : roles;

        String rolesString = String.join(",", roles);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("roles", rolesString)
                .signWith(secretKey, SignatureAlgorithm.HS512)  // HS512 사용
                .compact();
    }

    // 토큰에서 사용자 이름 추출
    public String getUsernameFromToken(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    // 토큰에서 권한 정보 추출
    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        String roles = claims.get("roles", String.class);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);  // 서명 검증
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired");
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token");
        } catch (Exception e) {
            System.out.println("Invalid JWT token: " + e.getMessage());  // 예외 메시지 출력
        }
        return false;
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getRefreshTokenExpiration());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)  // HS512 사용
                .compact();
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }
}
