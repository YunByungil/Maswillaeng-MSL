package Maswillaeng.MSLback.dto.post.reponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeResponseDto {

    private Long userId;
    private Long writerId;
    private Long postId;
    private Long likeCount;

    public PostLikeResponseDto(Long userId, Long writerId, Long postId, Long likeCount) {
        this.userId = userId;
        this.writerId = writerId;
        this.postId = postId;
        this.likeCount = likeCount;
    }
}
