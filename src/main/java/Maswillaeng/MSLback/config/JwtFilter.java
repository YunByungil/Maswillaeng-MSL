package Maswillaeng.MSLback.config;

import Maswillaeng.MSLback.auth.PrincipalDetails;
import Maswillaeng.MSLback.auth.PrincipalDetailsService;
import Maswillaeng.MSLback.common.exception.ErrorCode;
import Maswillaeng.MSLback.domain.entity.User;
import Maswillaeng.MSLback.service.UserService;
import Maswillaeng.MSLback.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static Maswillaeng.MSLback.common.exception.ErrorCode.*;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final PrincipalDetailsService principalDetailsService;

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /*
        TODO: PrincipalDetails 구현
        TODO: 비회원일 때 처리
        TODO: 어떻게 하면 액세스 토큰이 만료 되었을 때 필터를 거치지 않고 발급 받을 수 있을까?
        TODO: 로그인 상태가 아닌데 쿠키값이 존재하면?
         */
        log.info("=== doFilterInternal ===");

        /* 쿠키에서 토큰 꺼내기 */


        Cookie[] cookies = request.getCookies();
        String accessCookie = "";

        log.info("쿠키에서 토큰 꺼내기");

        if (!ObjectUtils.isEmpty(cookies)) {
            log.info("쿠키의 값이 존재, 로그인 상태임");

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken")) {
                    accessCookie = cookie.getValue();
                } else {
                    System.out.println("cookie.getName() = " + cookie.getName());
                    System.out.println("cookie.getPath() = " + cookie.getPath());
                    System.out.println("무슨 쿠키 인가요? value = " + cookie.getValue());
                }
            }

            log.info("SecretKey4 : {}");

            /**
             * 필터에서는 엑세스 토큰만 검증하자.
             * 엑세스 토큰 만료 -> 401
             */
            if (jwtUtil.isExpired(accessCookie)) { // 유효한 엑세스 토큰
                // TODO: isExpired 예외 처리 꼼꼼하게 더 하기
                log.info("Filter: 엑세스 토큰 유효하다");
                exceptionAccess(request, response, filterChain, accessCookie);
            } else {
                log.error("Filter: 엑세스 토큰 만료되었음");
                exceptionAccess(request, response, filterChain, accessCookie);
            }
        } else {
            filterChain.doFilter(request, response);
        }
//        log.info("SecretKey5 : {}", secretKey);

    }


    private void exceptionAccess(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String accessCookie) throws ServletException, IOException {
        // TODO: userId를 넘기지 말고 principalDetails를 넘겨서 원하는 값을 쓸 수 있게 하자.
        try {
            log.info("엑세스토큰 시작점");
            Long userId = jwtUtil.getUserId(accessCookie);
            User user = userService.findOne(userId);
//            UserDetails userDetails = principalDetailsService.loadUserByUsername(user.getEmail());
            PrincipalDetails principalDetails = new PrincipalDetails(user);
//            System.out.println("principalDetails.getAuthorities() = " + principalDetails.getAuthorities().toString());
//            System.out.println("principalDetails.getPassword() = " + principalDetails.getPassword());
//            System.out.println("principalDetails.getUsername() = " + principalDetails.getUsername());
//            System.out.println("principalDetails.getId() = " + principalDetails.getId());
//            System.out.println("userDetails = " + userDetails.getUsername());
//            System.out.println("userDetails = " + userDetails.getAuthorities());
            System.out.println("액세스토큰 = " + userId);
            // 권한 부여
//            UsernamePasswordAuthenticationToken authentication
//                    = new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
//            System.out.println("user.getRole() = " + user.getRole());
//            System.out.println("user.getRole().toString() = " + user.getRole().toString());
            UsernamePasswordAuthenticationToken authentication
                    = new UsernamePasswordAuthenticationToken(userId, null, principalDetails.getAuthorities());
            System.out.println("액세스토큰 = " + userId);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            System.out.println("액세스토큰 = " + userId);
            SecurityContextHolder.getContext().setAuthentication(authentication);
//            System.out.println("액세스토큰 = " + userId);

        } catch (SecurityException | MalformedJwtException | ExpiredJwtException e) {
            log.info("엑세스 토큰 유효기간 만료되었음!!");
            request.setAttribute("exception", INVALID_TOKEN.name());
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            log.info("권한이 없는데용?");
            request.setAttribute("exception", INVALID_PERMISSION.name());
        } catch (Exception e) {
            log.info("NullException, 1) 없는 회원");
            e.printStackTrace();
            request.setAttribute("exception", UNKNOWN_ERROR.name());
        } finally {
            log.info("여길 지나네");
            filterChain.doFilter(request, response);
        }
    }


}
