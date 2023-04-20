package Maswillaeng.MSLback.dto.comment.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentLikeAndHateResponse<T> {

    private int code;
    private String message;
    private T result;
}
