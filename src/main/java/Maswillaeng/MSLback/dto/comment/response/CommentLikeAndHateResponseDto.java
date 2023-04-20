package Maswillaeng.MSLback.dto.comment.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLikeAndHateResponseDto {

    private Long userId;
    private Long writerId;
    private Long commentId;
    private Long likeCount;

    private Long hateCount;

    public CommentLikeAndHateResponseDto(Long userId, Long writerId, Long commentId, Long likeCount, Long hateCount) {
        this.userId = userId;
        this.writerId = writerId;
        this.commentId = commentId;
        this.likeCount = likeCount;
        this.hateCount = hateCount;
    }
}
