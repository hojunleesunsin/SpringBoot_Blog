package me.leehojun.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.leehojun.springbootdeveloper.config.jwt.TokenProvider;
import me.leehojun.springbootdeveloper.domain.User;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패시, 예외 발생
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
