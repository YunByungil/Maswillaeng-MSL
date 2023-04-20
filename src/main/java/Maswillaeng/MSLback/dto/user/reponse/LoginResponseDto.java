package Maswillaeng.MSLback.dto.user.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto<T> {

    private int code;
    private T result;



}
