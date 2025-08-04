package me.shinsunyoung.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.dto.CreateAccessTokenRequest;
import me.shinsunyoung.springbootdeveloper.dto.CreateAccessTokenResponse;

import me.shinsunyoung.springbootdeveloper.sevice.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final me.shinsunyoung.springbootdeveloper.sevice.TokenService tokenService;
    // RestController는 시작 주소에 api를 꼭 붙일 것
    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(
            @RequestBody CreateAccessTokenRequest request){
        // RefreshToken이 정상이면 AccessToken을 생성
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        //응답 코드를 Created로 반환데이터에 AccessToken을 설정하여 전달
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
