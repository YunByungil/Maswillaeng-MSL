package Maswillaeng.MSLback.common.exception;

import Maswillaeng.MSLback.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static Maswillaeng.MSLback.common.exception.ErrorCode.FORBIDDEN;
import static Maswillaeng.MSLback.common.exception.ErrorCode.INVALID_PERMISSION;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String exception = ErrorCode.FORBIDDEN.name();
        log.error("Exception = {}", exception);
        log.info("권한이 없습니다.");
        setErrorResponse(response, FORBIDDEN);
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
