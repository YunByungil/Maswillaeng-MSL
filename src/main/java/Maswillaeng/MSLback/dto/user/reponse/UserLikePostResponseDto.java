package Maswillaeng.MSLback.dto.user.reponse;

import Maswillaeng.MSLback.domain.entity.Post;
import Maswillaeng.MSLback.domain.entity.PostLike;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLikePostResponseDto {

    private Long postId;
    private String thumbnail;
    private Long likeCount;
    private Long commentCount;

    public UserLikePostResponseDto(PostLike post) {
        this.postId = post.getId();
        this.thumbnail = post.getPost().getThumbnail();
        this.likeCount = post.getPost().getPostLike().stream().count();
        this.commentCount = post.getPost().getComment().stream().count();
    }
}
