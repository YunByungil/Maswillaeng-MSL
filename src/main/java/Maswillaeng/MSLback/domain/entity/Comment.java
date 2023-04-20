package Maswillaeng.MSLback.domain.entity;

import Maswillaeng.MSLback.dto.comment.request.CommentUpdateDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<Comment> child = new ArrayList<>();

    @OneToMany(mappedBy = "comment")
    private List<CommentLike> commentLike = new ArrayList<>();
    @OneToMany(mappedBy = "comment")
    private List<CommentHate> commentHate = new ArrayList<>();

    /**
     * addComment
     * @param post
     * @param user
     * @param content
     * @param parent
     */
    @Builder
    public Comment(Post post, User user, String content, Comment parent) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.parent = parent;
    }


    public void updateComment(CommentUpdateDto dto) {
        this.content = dto.getContent();
    }

}
