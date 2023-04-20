package Maswillaeng.MSLback.dto.comment.response;

import Maswillaeng.MSLback.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
//@Builder
public class CommentResponseDto {

    private Long commentId;
    private Long userId;
    private String content;
    private String nickname;
    private String userImage;
    private Long postId;
    private LocalDateTime createAt;
    private boolean likeState;
    private boolean hateState;
    private Long childCount;
    private Long likeCount;
    private Long hateCount;

    public CommentResponseDto(Comment comment, Long userId) {
        this.commentId = comment.getId();
        this.userId = comment.getUser().getId();
        this.content = comment.getContent();
        this.nickname = comment.getUser().getNickname();
        this.userImage = comment.getUser().getUserImage();
        this.postId = comment.getPost().getId();
        this.createAt = comment.getCreateAt();
        this.likeState = comment.getCommentLike()
                .stream()
                .anyMatch(commentLike -> commentLike.getUser().getId().equals(userId));
        this.hateState = comment.getCommentHate()
                .stream()
                .anyMatch(commentHate -> commentHate.getUser().getId().equals(userId));
        this.likeCount = comment.getCommentLike().stream().count();
        this.hateCount = comment.getCommentHate().stream().count();
        this.childCount = comment.getChild().stream().count();
    }



    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.userId = comment.getUser().getId();
        this.content = comment.getContent();
        this.nickname = comment.getUser().getNickname();
        this.userImage = comment.getUser().getUserImage();
        this.postId = comment.getPost().getId();
        this.createAt = comment.getCreateAt();
        this.likeCount = comment.getCommentLike().stream().count();
        this.hateCount = comment.getCommentHate().stream().count();
        this.childCount = comment.getChild().stream().count();
    }
}
