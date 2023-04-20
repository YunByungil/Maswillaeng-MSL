package Maswillaeng.MSLback.dto.comment.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChildCommentResponse<T> {

    private T result;
}
