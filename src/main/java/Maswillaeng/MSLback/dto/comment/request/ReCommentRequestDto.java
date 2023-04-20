package Maswillaeng.MSLback.dto.comment.request;

import Maswillaeng.MSLback.domain.entity.BaseEntity;
import Maswillaeng.MSLback.domain.entity.Comment;
import Maswillaeng.MSLback.domain.entity.Post;
import Maswillaeng.MSLback.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReCommentRequestDto extends BaseEntity {

    private User user;
    private Post post;
    private String content;
    private Comment comment;

    public Comment toEntityReComment(Post post, User user, Comment comment) {
        return Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .parent(comment)
                .build();
    }

}
