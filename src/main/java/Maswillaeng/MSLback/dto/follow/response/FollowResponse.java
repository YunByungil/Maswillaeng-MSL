package Maswillaeng.MSLback.dto.follow.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FollowResponse<T> {
    private int code;
    private String message;
    private T result;
}
