package Maswillaeng.MSLback.common.exception;

import Maswillaeng.MSLback.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static Maswillaeng.MSLback.common.exception.ErrorCode.*;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // TODO: 더 추가해야 됨. JwtFilter
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "에러");
        String exception = (String) request.getAttribute("exception");
        if (exception == null) {
            log.info("Exception에서 null 발생");
            log.info("토큰이 존재하지 않습니다. 로그인 해주세여");
            setErrorResponse(response, FORBIDDEN);
        }

        else if (exception.equals(UNKNOWN_ERROR.name())) {
            log.info("알 수 없는 에러가 발생하였습니다.");
            setErrorResponse(response, UNKNOWN_ERROR);

        }
        else if (exception.equals(INVALID_TOKEN.name())) {
            log.info("토큰이 만료되었습니다.");
            setErrorResponse(response, INVALID_TOKEN);
//            response.sendRedirect("/api");
        }

        else if (exception.equals(INVALID_PERMISSION.name())) {
            log.info("권한이 없습니다.");
            setErrorResponse(response, INVALID_PERMISSION);
        }

        log.error("Exception = {}", exception);

    }


    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException{

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getStatus().value());

        Map<String, Object> result = new HashMap<>();
        result.put("errorCode", errorCode.name());
        result.put("message", errorCode.getMessage());

        response.getWriter().println(
                objectMapper.writeValueAsString(
                        ErrorResponse.error(errorCode.getStatus().value(), result)));
    }


}
