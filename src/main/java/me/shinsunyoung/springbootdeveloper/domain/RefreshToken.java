package me.shinsunyoung.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false)
    private Long    id;
    //실제 계정의 id값
    @Column(name = "user_id", nullable = false, unique = true)
    private Long    userId;
    // 사용자가 가지고 있는 리프레시토큰과 같은 데이터를 저장
    @Column(name = "refresh_token",nullable = false)
    private String    refreshToken;

    public RefreshToken(Long userId, String refreshToken) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
    //리프레시 토큰 갱신위한 메서드
    public RefreshToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }
}
