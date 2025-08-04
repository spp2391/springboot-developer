package me.shinsunyoung.springbootdeveloper.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;
import me.shinsunyoung.springbootdeveloper.config.jwt.JwtProperties;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Getter
public class JwtFactory {
    private String subject = "test@email.com";
    private Date issuedAt = new Date();
    private Date expiresAt = new Date(new Date().getTime()+ Duration.ofDays(14).toMillis());
    private Map<String, Object> claims = Collections.emptyMap();
    @Builder
    public JwtFactory(String subject,Date issuedAt,Date expiration,
            Map<String, Object> claims) {
        //JwtFactory 생성시 null 이 들어올 경우 기본값으로 설정한 데이터를 사용
        this.subject = subject !=null ? subject : this.subject;
        this.issuedAt = issuedAt!=null ? issuedAt : this.issuedAt;
        this.expiresAt = expiration!=null ? expiration : this.expiresAt;
        this.claims = claims !=null ? claims : this.claims;
    }
    public static JwtFactory withDefaultValues() {
        return JwtFactory.builder().build();
    }
    public String createToken(JwtProperties jwtProperties) {
        return Jwts.builder()
                .setSubject(subject)
                .setHeaderParam(Header.TYPE,Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }
}
