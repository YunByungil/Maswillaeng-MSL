package Maswillaeng.MSLback.utils;

import Maswillaeng.MSLback.domain.enums.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey; // 시크릿 키
    public static final Long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 10 * 60L; // AccessToken 시간 1분
    public static final Long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 120L; // RefreshToken 시간

    public String createJwt(Long userId, RoleType roleType) {
        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("role", roleType);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshJwt(Long userId) {
        Claims claims = Jwts.claims();
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public void reissueAccessToken(String refreshToken, String accessToken) {
        System.out.println("엑세스토큰 재발급 완료 메서드");
        System.out.println("secretKey = " + secretKey);
        Long userId = getUserId(refreshToken);
        System.out.println("userId = " + userId);

        String token = createJwt(userId, RoleType.USER);
        System.out.println("token = " + token);
//        cookieUtil.createAccessCookieToken()
    }


    /**
     * 유효 기간을 체크하는 메서드 Access
     * @param token Bearer를 제외한 토큰 정보
     */
    public boolean isExpired(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                    .getBody().getExpiration().before(new Date());
            return true;
        } catch (ExpiredJwtException | IllegalArgumentException e) {

            return false;
        }

    }

    /**
     * 로그인 시, 회원 id를 알 수 있는 메서드
     * @param token 토큰 정보
     */
    public Long getUserId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("userId", Long.class);
    }
}
