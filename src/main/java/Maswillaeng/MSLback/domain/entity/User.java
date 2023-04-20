package Maswillaeng.MSLback.domain.entity;

import Maswillaeng.MSLback.domain.enums.RoleType;
import Maswillaeng.MSLback.dto.user.request.UserUpdateDTO;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
public class User extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String nickname;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    @Column(length = 3000)
    private String userImage;
    private String introduction;
    @ColumnDefault("0")
    private int withdrawYn;
    @Enumerated(EnumType.STRING)
    private RoleType role;
    @Column(length = 3000)
    private String refresh_token;
    private LocalDateTime withdrawAt;


    @OneToMany(mappedBy = "user")
    private List<Post> post = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<PostLike> postLike = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.REMOVE)
    private List<Follow> followerList = new ArrayList<>();
    @OneToMany(mappedBy = "following", cascade = CascadeType.REMOVE)
    private List<Follow> followingList = new ArrayList<>();

    @Builder
    public User(String email, String password, String nickname,
                String phoneNumber, String userImage, String introduction) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.userImage = userImage;
        this.introduction = introduction;
        this.withdrawYn = 0;
        this.role = RoleType.USER;
    }

    public void updateUser(UserUpdateDTO userUpdateDTO, String pwd) {
        this.password = pwd;
        this.nickname = userUpdateDTO.getNickname();
        this.phoneNumber = userUpdateDTO.getPhoneNumber();
        this.userImage = userUpdateDTO.getUserImage();
        this.introduction = userUpdateDTO.getIntroduction();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refresh_token = refreshToken;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void deleteRefreshToken() {
        this.refresh_token = null;
    }
}
