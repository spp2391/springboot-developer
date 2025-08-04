package me.shinsunyoung.springbootdeveloper.jwt;

import io.jsonwebtoken.Jwts;
import me.shinsunyoung.springbootdeveloper.config.jwt.JwtProperties;
import me.shinsunyoung.springbootdeveloper.config.jwt.TokenProvider;
import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProperties jwtProperties;
    @DisplayName("generateToken() : 유저정보와 만료기간을 전달해 토큰을 만들수있다")
    @Test
    void generateToken() {

        User testUser = userRepository.save(
                User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());
        //토큰 생성 유효기간 14일인 토큰생성
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));
        //토큰을 콘솔창에 출력
        System.out.println(token);
        //토큰안에 있는 유저아이디 저장
        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id",Long.class);
        //토큰의 userid와  직접생성한 임시데이터의 userId가 일치하면 테스트결과 정상출력
        assertThat(userId).isEqualTo(testUser.getId());
    }
    @DisplayName("validToken(): 만료된 토큰인 때에 유효성 검증에 실패한다.")
    @Test
    void validToken_invalidToken() {
        //만료일을 현재날짜 -7일로 만들어 만료된 토큰을 생성
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() -Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);
        //토큰이 정상인지확인하는 메서드를 실행
        boolean result = tokenProvider.validToken(token);
        //토큰 확인 결과가 false로 나오면 테스트 통과

        assertThat(result).isFalse();
    }
    @DisplayName("getAuthentication(): 토큰 기반으로 인증 정보를 가져올 수 있다.")
    @Test
    void getAuthentication(){
        // 임시 이메일 설정
        String userEmail = "user@email.com";
        // 임시 이메일로 토큰을 생성
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);
        //위에서 만든 토큰으로 인증 정보를 저장
        Authentication authentication = tokenProvider.getAuthentication(token);
        // userId 정보
        System.out.println(((UserDetails)authentication.getPrincipal()).getUsername());
        // password
        System.out.println(((UserDetails)authentication.getPrincipal()).getPassword());
        // 권한
        System.out.println(((UserDetails)authentication.getPrincipal()).getAuthorities());
        assertThat(((UserDetails)authentication.getPrincipal()).getUsername())
                .isEqualTo(userEmail);
    }
    @DisplayName("getUserId():토큰으로 유저ID를 가져 올 수있다")
    @Test
    void getUserId(){
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id",userId))
                .build()
                .createToken(jwtProperties);
        //토큰에 저장된 userId를 저장
        Long userIdBytoken  =tokenProvider.getUserId(token);
        //토큰userId 와 저장한 userId가 일치하는지 확인
        assertThat(userIdBytoken).isEqualTo(userId);
    }
}
