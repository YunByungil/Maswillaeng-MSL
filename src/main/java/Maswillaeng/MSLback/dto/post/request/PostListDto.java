package Maswillaeng.MSLback.dto.post.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostListDto {

    private Long post_id;
    private String nickname;
    private String thumbnail;
    private String title;
}
