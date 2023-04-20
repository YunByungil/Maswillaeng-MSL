package Maswillaeng.MSLback.domain.entity;

import Maswillaeng.MSLback.domain.enums.Category;
import Maswillaeng.MSLback.dto.post.request.PostUpdateRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private String thumbnail;
    @Column(length = 100, nullable = false)
    private String title;
    @Column(length = 100, nullable = false)
    private String content;
    private Long hits;
    private int report;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comment = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<PostLike> postLike = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<HashTag> hashTag = new ArrayList<>();
    /*
    테스트
     */
//    private List<String> image = new ArrayList<>();
//    @Builder
//    public Post(User user, String thumbnail, String title, String content, Category category, List<String> list) {
//        this.user = user;
//        this.thumbnail = thumbnail;
//        this.title = title;
//        this.content = content;
//        this.hits = 0L;
//        this.report = 0;
//        this.category = category;
//        this.image = list;
//    }

    @Builder
    public Post(User user, String thumbnail, String title, String content, Category category) {
        this.user = user;
        this.thumbnail = thumbnail;
        this.title = title;
        this.content = content;
        this.hits = 0L;
        this.report = 0;
        this.category = category;
    }

    public void update(PostUpdateRequestDto dto) {
        this.thumbnail = dto.getThumbnail();
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.category = dto.getCategory();
    }

    public void addHashTag(List<HashTag> tag) {
        this.hashTag = tag;
    }
}
