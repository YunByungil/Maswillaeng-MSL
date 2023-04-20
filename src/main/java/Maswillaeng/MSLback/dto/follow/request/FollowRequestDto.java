package Maswillaeng.MSLback.dto.follow.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FollowRequestDto {

    private Long userId;
    private Long myId;
    private Long userFollowerCount;
}
