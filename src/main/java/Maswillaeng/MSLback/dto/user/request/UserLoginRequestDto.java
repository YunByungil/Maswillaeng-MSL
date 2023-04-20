package Maswillaeng.MSLback.dto.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequestDto {

    private String email;
    private String password;
}
