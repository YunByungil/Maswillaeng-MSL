package Maswillaeng.MSLback.service;

import Maswillaeng.MSLback.domain.entity.Follow;
import Maswillaeng.MSLback.domain.entity.Post;
import Maswillaeng.MSLback.domain.entity.PostLike;
import Maswillaeng.MSLback.domain.entity.User;
import Maswillaeng.MSLback.domain.repository.PostLikeRepository;
import Maswillaeng.MSLback.domain.repository.PostRepository;
import Maswillaeng.MSLback.domain.repository.UserRepository;
import Maswillaeng.MSLback.dto.user.reponse.TokenResponse;
import Maswillaeng.MSLback.dto.user.reponse.UserDetailResponseDto;
import Maswillaeng.MSLback.dto.user.reponse.UserLoginResponseDto;
import Maswillaeng.MSLback.dto.user.request.UserJoinDTO;
import Maswillaeng.MSLback.dto.user.request.UserListDTO;
import Maswillaeng.MSLback.dto.user.request.UserUpdateDTO;
import Maswillaeng.MSLback.utils.CookieUtil;
import Maswillaeng.MSLback.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ValidateService validateService;
    private final CookieUtil cookieUtil;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret}")
    private String secretKey; // 시크릿 키

    @Transactional
    public UserLoginResponseDto login(Long userId) {
//        /* 유저 존재 여부 확인 */
        User user = findOne(userId);
//
//        /* 비밀번호 맞는지 확인 */
//        if(!encoder.matches(dto.getPassword(), user.getPassword())) {
//            throw new IllegalStateException("비밀번호 틀림");
//        }

        String accessToken = jwtUtil.createJwt(user.getId(), user.getRole());
        String refreshToken = jwtUtil.createRefreshJwt(user.getId());
        user.updateRefreshToken(refreshToken);
        System.out.println("refreshToken = " + refreshToken);
//        userRepository.save(user);
        TokenResponse token = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return UserLoginResponseDto.builder()
                .tokenResponse(token)
                .nickname(user.getNickname())
                .userImage(user.getUserImage())
                .build();
    }
//    @Transactional
//    public UserLoginResponseDto login(UserLoginRequestDto dto) {
////        /* 유저 존재 여부 확인 */
////        User user = validateService.validateExistUser(dto);
////
////        /* 비밀번호 맞는지 확인 */
////        if(!encoder.matches(dto.getPassword(), user.getPassword())) {
////            throw new IllegalStateException("비밀번호 틀림");
////        }
//
//        String accessToken = jwtUtil.createJwt(user.getId(), user.getRole());
//        String refreshToken = jwtUtil.createRefreshJwt(user.getId());
//        user.updateRefreshToken(refreshToken);
//        System.out.println("refreshToken = " + refreshToken);
////        userRepository.save(user);
//        TokenResponse token = TokenResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//
//        return UserLoginResponseDto.builder()
//                .tokenResponse(token)
//                .nickname(user.getNickname())
//                .userImage(user.getUserImage())
//                .build();
//    }

    @Transactional
    public Long join(UserJoinDTO dto) {
//        validateDuplicateEmail(user.getEmail());
        checkNicknameDuplicate(dto.getNickname());
        checkEmailDuplicate(dto.getEmail());
        checkPhoneNumberDuplicate(dto.getPhoneNumber());
        User user = userRepository.save(dto.toEntity(encoder.encode(dto.getPassword())));
        return user.getId();
    }

    @Transactional
    public void update(Long userId, UserUpdateDTO dto) {
        User user = findOne(userId);

        user.updateUser(dto, encoder.encode(dto.getPassword()));
    }

    public User findOne(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("없는 회원"));
        return user;
    }

    /**
     * 유저 조회 테스트
     * 생쿼리로 날린 거라 쿼리 수정해야 됨.
     */
    public UserDetailResponseDto testMember(Long userId, Long myId) {
        List<PostLike> findPostLikeList = postLikeRepository.findPostLikeByUserIdAndFetchJoinPost(userId);
        // 내 정보일 때,
        if (userId == myId) {
            User user = findOne(myId);
            List<Post> findPostList = postRepository.findByUserId(myId);
            return new UserDetailResponseDto(user, true, findPostList, findPostLikeList);
        }
        // 남 정보일 때,
        for (PostLike postLike : findPostLikeList) {
            System.out.println("postLike = " + postLike.getPost().getTitle());
            System.out.println("postLike.getPost().getContent() = " + postLike.getPost().getContent());
        }
        /*
        TODO: likePost 불러올 때, 쿼리 개선하기
         */
        User findUser = findOne(userId);
        List<Post> findPostList = postRepository.findByUserId(userId);
//        boolean status = false;
        boolean status = findUser.getFollowingList().stream()
                .filter(f -> f.getFollower().getId().equals(myId))
                .findFirst()
                .isPresent();
        return new UserDetailResponseDto(findUser, status, findPostList, findPostLikeList);
    }

    /**
     * 비로그인 상태로 user정보 확인
     */
    public UserDetailResponseDto noLoginAndGetMember(Long userId) {
        List<PostLike> findPostLikeList = postLikeRepository.findPostLikeByUserIdAndFetchJoinPost(userId);
        User findUser = findOne(userId);
        List<Post> findPostList = postRepository.findByUserId(userId);
        return new UserDetailResponseDto(findUser, false, findPostList, findPostLikeList);
    }


    public List<UserListDTO> findAll() {
        List<User> allUser = userRepository.findAll();

        return allUser.stream()
                .map(u -> new UserListDTO(u))
                .collect(Collectors.toList());
    }

//    public void validateDuplicateEmail(String email) {
//        List<User> userEmail = userRepository.findByEmail(email);
//        if (!userEmail.isEmpty()) {
//            throw new IllegalStateException("이미 존재하는 Email입니다.");
//        }
//    }

    public TokenResponse reissueAccessToken(String refreshToken) {
        /**
         * 토큰 발급 시간 어떻게 할지
         */
        String token = "";
        System.out.println("엑세스토큰 재발급 완료 메서드");
        System.out.println("\"\" = " + "확인요");
        Long userId = jwtUtil.getUserId(refreshToken);
        System.out.println("userId = " + userId);
        User user = findOne(userId);
        if (user.getRefresh_token().equals(refreshToken)) {
            token = jwtUtil.createJwt(userId, user.getRole());
            System.out.println("token = " + token);
        } else {
            new Exception("이상한 토큰을 넣었음!");
        }
        return TokenResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
//        return ResponseEntity.ok()
//                .header("Set-Cookie", token)
//                .header("Set-Cookie", refreshToken)
//                .body("");

    }

    @Transactional
    public void deleteRefreshToken(Long userId) {
        User findUser = findOne(userId);
        findUser.deleteRefreshToken();
    }

    public void checkEmailDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("이미 존재하는 Email입니다.");
        }
    }
    public void checkNicknameDuplicate(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalStateException("이미 존재하는 Nickname입니다.");
        }
    }
    public void checkPhoneNumberDuplicate(String phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalStateException("이미 존재하는 PhoneNumber입니다.");
        }
    }
}
