package me.shinsunyoung.springbootdeveloper.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    private final JwtProperties jwtProperties;
    public String generateToken(User user, Duration expiredAt){
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }
    private String makeToken(Date expiry, User user){
        Date now = new Date();
        return Jwts.builder()
                // JWT 타입 설정 : "typ":"JWT"
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                // "iss" : "ajufresh@gmail.com"
                .setIssuer(jwtProperties.getIssuer())
                // 현재시간 설정, "iat" : "2025-07-28 10:14:30"
                .setIssuedAt(now)
                // 만료시간 설정 , "exp" : "2025-07-28 11:14:30"
                // Access Token, Refresh Token의 종류에 따라 다르게 설정됨
                .setExpiration(expiry)
                // "sub" : "로그인 유저의 이메일"
                .setSubject(user.getEmail())
                // 저장하고 싶은 데이터를 claim으로 저장 가능
                // "id" : "hong"
                .claim("id",user.getId())
                // 서명에서 사용할 암호화 알고리즘 및 키값
                // "alg" : "HS256"
                // HS256을 사용하여 secretKey를 암호화한 값을 서명 부분에 저장
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }
    public boolean validToken(String token){
        try{
            // jwt토큰이 정상인지 확인하여 정상이면 true반환
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){
            // 비정상인경우 false반환
            return false;
        }
    }
    public Authentication getAuthentication(String token){
        // Token안의 내용(claims)을 저장
        Claims claims = getClaims(token);
        // 권한을 ROLE_USER로 설정
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        // Token을 이용하여 SpringSecurity 로그인 객체를 생성
        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(
                        claims.getSubject(), "" , authorities), token, authorities);
    }
    // Token에서 내용(claims)을 꺼내는 메서드
    private Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
    // Token생성시Claims안에 저장된 id를 반환하는 메서드
    public Long getUserId(String token){
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }
}












