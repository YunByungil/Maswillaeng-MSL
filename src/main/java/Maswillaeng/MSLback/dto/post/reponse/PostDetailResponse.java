package Maswillaeng.MSLback.dto.post.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponse<T> {

    private int code;
    private T result;

}
