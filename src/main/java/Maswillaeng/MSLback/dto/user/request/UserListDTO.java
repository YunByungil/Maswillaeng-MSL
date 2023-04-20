package Maswillaeng.MSLback.dto.user.request;

import Maswillaeng.MSLback.domain.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserListDTO {
    private Long userId;
    private String email;
    private String nickname;
    private String phoneNumber;

    public UserListDTO(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.phoneNumber = user.getPhoneNumber();
    }
}
