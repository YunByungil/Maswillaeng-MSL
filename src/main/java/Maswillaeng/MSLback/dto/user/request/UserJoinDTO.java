package Maswillaeng.MSLback.dto.user.request;

import Maswillaeng.MSLback.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserJoinDTO {

    private String email;
    private String password;
    private String nickname;
    private String phoneNumber;
    private String userImage;
    private String introduction;

    public User toEntity(String password) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .userImage(userImage)
                .introduction(introduction)
                .build();
    }
}
