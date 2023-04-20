package Maswillaeng.MSLback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse<T> {

    private int code;
    private T result;

    public static <T> ErrorResponse<T> error(int code, T result) {
        return new ErrorResponse<>(code, result);
    }
}
