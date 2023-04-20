package Maswillaeng.MSLback.auth;

import Maswillaeng.MSLback.service.UserService;
import Maswillaeng.MSLback.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Slf4j
@Component
public class LogoutFilter implements LogoutHandler {

    private final UserService userService;
    private final CookieUtil cookieUtil;

    /*
    LogoutFilter 이미 인증 객체가 제거된 상태이므로 authentication이 null값일 수 밖에 없다.
    TODO: 로그아웃 필터 사용 -> RefreshToken null로 바꿀 수 없음
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("logoutFilter 진입");
//        authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(ObjectUtils.isEmpty(authentication));
//        Long userId = Long.parseLong(authentication.getName());
//        userService.deleteRefreshToken(userId);
//
//        ResponseCookie cookie = cookieUtil.deleteAccessCookieToken();
//        ResponseCookie cookie2 = cookieUtil.deleteRefreshCookieToken();
        log.info("logout 완료");

//        response.setHeader("Set-Cookie", cookie.toString());
//        response.setHeader("Set-Cookie", cookie2.toString());
    }
}
