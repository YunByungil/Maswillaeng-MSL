package Maswillaeng.MSLback.utils;

import Maswillaeng.MSLback.dto.user.reponse.UserLoginResponseDto;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieUtil {

    public ResponseCookie createAccessCookieToken(UserLoginResponseDto userLoginResponseDto) {
        return ResponseCookie
                .from("accessToken", userLoginResponseDto.getTokenResponse().getAccessToken())
                .path("/")
                .httpOnly(true)
                // 시간
                .maxAge(JwtUtil.REFRESH_TOKEN_EXPIRE_TIME)
                .sameSite("Lax")
                .build();
    }

    /**
     * 재발급 엑세스 토큰
     * @param accessToken
     * @return
     */
    public ResponseCookie createCookie(String accessToken) {
        return ResponseCookie
                .from("accessToken", accessToken)
                .path("/")
                .httpOnly(true)
                // 시간
                .maxAge(JwtUtil.REFRESH_TOKEN_EXPIRE_TIME)
                .sameSite("Lax")
                .build();
    }

    public ResponseCookie createRefreshCookieToken(UserLoginResponseDto userLoginResponseDto) {
        /*
        path: 위치 바꿀까?
         */
        return ResponseCookie
                .from("refreshToken", userLoginResponseDto.getTokenResponse().getRefreshToken())
                .path("/api/token")
                .httpOnly(true)
                .maxAge(JwtUtil.REFRESH_TOKEN_EXPIRE_TIME)
                .sameSite("Lax")
                .build();
    }

    /**
     * 쿠키 삭제 메서드
     * logout할 때 사용
     */
    public ResponseCookie deleteAccessCookieToken() {
        return ResponseCookie
                .from("accessToken", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .sameSite("Lax")
                .build();
    }
    public ResponseCookie deleteRefreshCookieToken() {
        return ResponseCookie
                .from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .sameSite("Lax")
                .build();
    }
}
