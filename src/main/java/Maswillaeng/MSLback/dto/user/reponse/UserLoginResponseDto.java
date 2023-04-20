package Maswillaeng.MSLback.dto.user.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginResponseDto {

    private TokenResponse tokenResponse;
    private String nickname;
    private String userImage;
}
