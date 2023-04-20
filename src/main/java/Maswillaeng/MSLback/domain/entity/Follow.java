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
public class Follow {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "following_id")
    private User following;

    @Builder
    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }
}
