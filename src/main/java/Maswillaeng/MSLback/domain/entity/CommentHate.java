package Maswillaeng.MSLback.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentHate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_hate_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Builder
    public CommentHate(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }
}
