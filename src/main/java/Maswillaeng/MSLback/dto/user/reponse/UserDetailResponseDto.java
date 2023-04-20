package Maswillaeng.MSLback.dto.user.reponse;

import Maswillaeng.MSLback.domain.entity.Post;
import Maswillaeng.MSLback.domain.entity.PostLike;
import Maswillaeng.MSLback.domain.entity.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserDetailResponseDto {
    /**
     * 임시 class,
     */
    @Getter
    static class userPost {
        private Long postId;
        private String thumbnail;
        private Long likeCount;
        private Long commentCount;

        public userPost(Post post) {
            this.postId = post.getId();
            this.thumbnail = post.getThumbnail();
            this.likeCount = post.getPostLike().stream().count();
            this.commentCount = post.getComment().stream().count();
        }
    }

    private Long userId;
    private String nickname;
    private String userImage;
    private String introduction;
    private boolean followState;
    private int followerCount;
    private int followingCount;
    private List<userPost> postList = new ArrayList<>();
    private List<UserLikePostResponseDto> likePostList = new ArrayList<>();

    public UserDetailResponseDto(User user, boolean status, List<Post> post, List<PostLike> postLike) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.userImage = user.getUserImage();
        this.introduction = user.getIntroduction();
        this.followState = status;
        this.followerCount = user.getFollowingList().size();
        this.followingCount = user.getFollowerList().size();
        this.postList = post.stream().map(p -> new userPost(p)).collect(Collectors.toList());
        this.likePostList = postLike.stream()
                .map(p -> new UserLikePostResponseDto(p))
                .collect(Collectors.toList());
    }
}


