package Maswillaeng.MSLback.auth;

import Maswillaeng.MSLback.common.exception.ErrorCode;
import Maswillaeng.MSLback.domain.entity.User;
import Maswillaeng.MSLback.dto.ErrorResponse;
import Maswillaeng.MSLback.dto.user.reponse.UserLoginResponseDto;
import Maswillaeng.MSLback.service.UserService;
import Maswillaeng.MSLback.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter  extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /*
    /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication 함수 실행: 로그인 시도 중");
        try {

            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            log.info("user = {}",  user);

            // PrincipalDetailsService의 loadUsrByUsername() 함수가 실행된 후 정상이면 authentication이 리턴 됨
            // Db에 있는 username과 password가 일치한다.
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()); // 토큰 생성
            log.info("user.getUsername() = {}", user.getEmail());
            log.info("user.getPassword() = {}", user.getPassword());

            // authentication 객체가 session 영역에 저장 됨 => 로그인이 되었다는 뜻.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.info("로그인 완료됨: {}", principalDetails.getUser().getEmail()); // 로그인 정상 완료

            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    /login 성공했을 때,
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("successfulAuthentication 함수 실행: 로그인 성공");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        Long userId = principalDetails.getId();
        UserLoginResponseDto login = userService.login(principalDetails.getId());
        ResponseCookie accessToken = ResponseCookie
                .from("accessToken", login.getTokenResponse().getAccessToken())
                .path("/")
                .httpOnly(true)
                // 시간
                .maxAge(JwtUtil.REFRESH_TOKEN_EXPIRE_TIME)
                .sameSite("Lax")
                .build();

        ResponseCookie refreshToken = ResponseCookie
                .from("refreshToken", login.getTokenResponse().getRefreshToken())
                .path("/api/token")
                .httpOnly(true)
                .maxAge(JwtUtil.REFRESH_TOKEN_EXPIRE_TIME)
                .sameSite("Lax")
                .build();

        /*
        set은 Set-Cookie로 만든 거를 덮어 씌움
        add는 계속 추가 add로 구현해야 됨!
         */
        response.addHeader("Set-Cookie", accessToken.toString());
        response.addHeader("Set-Cookie", refreshToken.toString());


        setAuthResponse(response, JwtUtil.ACCESS_TOKEN_EXPIRE_TIME, userId);
        /*
        로그인 완료,
        1. 토큰 생성
        2. 쿠키 장착
         */
//        response.sendRedirect("/api/loginTest");
//        super.successfulAuthentication(request, response, chain, authResult);
    }


    /*
    /login 실패했을 때,
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("unsuccessfulAuthentication 함수 실행: 로그인 실패!!!!");
        super.unsuccessfulAuthentication(request, response, failed);
    }

    private void setAuthResponse(HttpServletResponse response, Long tokenMinute, Long userId) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        long hours = ((tokenMinute / (1000 * 60 * 60)) % 24);
        long minutes = ((tokenMinute / (1000 * 60)) % 60);
        long seconds = (tokenMinute / 1000) % 60;

        String remainingTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        result.put("expirationTime", remainingTime);

        response.getWriter().println(
                objectMapper.writeValueAsString(
                        AuthResponse.authResponse(HttpServletResponse.SC_OK, result)));
    }
}
