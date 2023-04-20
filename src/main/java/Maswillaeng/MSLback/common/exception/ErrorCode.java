package Maswillaeng.MSLback.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 기간이 만료되었습니다."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자가 권한이 없습니다."),
    UNKNOWN_ERROR(HttpStatus.BAD_REQUEST, "알 수 없는 에러가 발생하였습니다."),

    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다.");

    private HttpStatus status;
    private String message;
}
