package Maswillaeng.MSLback.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Builder
    public CommentLike(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }
}
