package Maswillaeng.MSLback.dto.post.reponse;

import Maswillaeng.MSLback.domain.entity.HashTag;
import Maswillaeng.MSLback.domain.entity.Post;
import Maswillaeng.MSLback.domain.enums.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchTestDto {

    private Long postId;
    private Long userId;
    private String nickname;
    private String thumbnail;
    private String title;
    private String content;
    private Long hits;
    private Category category;
    private Long commentCount;
    private Long likeCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createAt;
    private List<String> hashTag = new ArrayList<>();

    public SearchTestDto(Post post, int commentCount, int likeCount) {
        this.postId = post.getId();
        this.userId = post.getUser().getId();
        this.nickname = post.getUser().getNickname();
        this.thumbnail = post.getThumbnail();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.hits = post.getHits();
        this.category = post.getCategory();
        this.commentCount = (long)commentCount;
        this.likeCount = (long)likeCount;
        this.createAt = post.getCreateAt();
        this.hashTag = post.getHashTag().stream()
                .map(t -> t.getTag().getName())
                .collect(Collectors.toList());
    }
}
