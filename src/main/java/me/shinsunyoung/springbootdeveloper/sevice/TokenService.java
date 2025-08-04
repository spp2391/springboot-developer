package me.shinsunyoung.springbootdeveloper.sevice;



import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.config.jwt.TokenProvider;
import me.shinsunyoung.springbootdeveloper.domain.User;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;


    public String createNewAccessToken(String refreshToken){
        // RefreshToken이 만료되었을 경우 에러 발생
        if(!tokenProvider.validToken(refreshToken)){
            throw new IllegalArgumentException("Invalid refresh token");
        }
        // RefreshToken이 정상이라면 UserId를 저장
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        // UserId로 User데이터를 저장
        User user = userService.findById(userId);
        // 새로운 엑세스 토큰을 만들어서 반환
        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}

