package Maswillaeng.MSLback.dto.post.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponse<T> {

    private int totalCount;
    private int code;
    private T result;

}
