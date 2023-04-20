package Maswillaeng.MSLback.dto.post.request;

import Maswillaeng.MSLback.domain.entity.Post;
import Maswillaeng.MSLback.domain.enums.Category;
import Maswillaeng.MSLback.dto.comment.response.CommentResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailDto {

    private Long postId;
    private Long writerId;
    private String nickname;
    private String userImage;
    private String thumbnail;
    private String title;
    private String content;
    private Category category;
    private Long hits;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createAt;

    private boolean likeState;
    private boolean followState;
    private Long likeCount;
    private Long commentCount;

    private List<String> tag = new ArrayList<>();
    private List<CommentResponseDto> comment = new ArrayList<>();

    /**
     * 로그인
     */
    public PostDetailDto(Post post, boolean state, boolean isFollow, Long userId) {
        this.postId = post.getId();
        this.writerId = post.getUser().getId();
        this.nickname = post.getUser().getNickname();
        this.userImage = post.getUser().getUserImage();
        this.thumbnail = post.getThumbnail();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.hits = post.getHits();
        this.createAt = post.getCreateAt();

        this.likeState = state;
        this.followState = isFollow;
        this.likeCount = post.getPostLike().stream().count();
        this.commentCount = post.getComment().stream().count();

        this.tag = post.getHashTag().stream()
                .map(t -> t.getTag().getName())
                .collect(Collectors.toList());
        this.comment = post.getComment().stream()
                .filter(c -> c.getParent() == null)
                .map(c -> new CommentResponseDto(c, userId))
                .collect(Collectors.toList());
    }

    /**
     * 비로그인
     */
    public PostDetailDto(Post post, boolean state) {
        this.postId = post.getId();
        this.nickname = post.getUser().getNickname();
        this.userImage = post.getUser().getUserImage();
        this.thumbnail = post.getThumbnail();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.createAt = post.getCreateAt();

        this.likeState = state;
        this.likeCount = post.getPostLike().stream().count();
        this.commentCount = post.getComment().stream().count();

        this.tag = post.getHashTag().stream()
                .map(t -> t.getTag().getName())
                .collect(Collectors.toList());
        this.comment = post.getComment().stream()
                .filter(c -> c.getParent() == null)
                .map(c -> new CommentResponseDto(c))
                .collect(Collectors.toList());
    }
}
