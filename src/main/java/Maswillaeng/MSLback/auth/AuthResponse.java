package Maswillaeng.MSLback.auth;

import Maswillaeng.MSLback.dto.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse<T> {

    private int code;
    private T result;

    public static <T> AuthResponse<T> authResponse(int code, T result) {
        return new AuthResponse<>(code, result);
    }
}
