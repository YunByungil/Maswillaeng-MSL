package Maswillaeng.MSLback.dto.user.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginResultResponse {

    private String nickname;
    private String userImage;
}
