package Maswillaeng.MSLback.service;

import Maswillaeng.MSLback.domain.entity.Follow;
import Maswillaeng.MSLback.domain.entity.HashTag;
import Maswillaeng.MSLback.domain.entity.Post;
import Maswillaeng.MSLback.domain.entity.User;
import Maswillaeng.MSLback.domain.repository.FollowRepository;
import Maswillaeng.MSLback.domain.repository.PostLikeRepository;
import Maswillaeng.MSLback.domain.repository.PostRepository;
import Maswillaeng.MSLback.domain.repository.UserRepository;
import Maswillaeng.MSLback.dto.post.request.PostDetailDto;
import Maswillaeng.MSLback.dto.post.reponse.PostListResponseDto;
import Maswillaeng.MSLback.dto.post.request.PostRequestDto;
import Maswillaeng.MSLback.dto.post.request.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final HashTagService hashTagService;
    private final PostLikeRepository postLikeRepository;
    private final FollowRepository followRepository;

    @Transactional
    public void addPost(Long id, PostRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalStateException("없는 회원")
                );

        Post post = dto.toEntity(user);
        postRepository.save(post);
        hashTagService.addHashTag(post, dto.getTag());
//        for (HashTag hashTag : hashTags) {
//            System.out.println("hashTag = " + hashTag.getTag());
//            System.out.println("hashTag.getPost() = " + hashTag.getPost());
//        }
    }

    /**
     * 게시글 수정
     * @param id
     * @param dto
     */
    @Transactional
    public void updatePost(Long id, PostUpdateRequestDto dto) {
        // TODO: 내가 쓴 게시글이 맞는지 검증 로직 추가
        /*
        굳이 게시글 하나만 찾는 findOne이 필요할까?
        그냥 Repository에서 바로 불러와도 될 거 같은뎅
        근데 Optional때문에 놔둬도 될 거 같기도 하고 (가독성)
         */
        Post post = findOne(id);
        List<String> tag = dto.getTag();
//        List<HashTag> postTag = post.getHashTag();
        hashTagService.updateHashTag(post, tag);
        hashTagService.addHashTag(post, tag);
        post.update(dto);
    }


    /**
     * 게시글 상세 조회
     * 이 로직은 could not initialize proxy [Maswillaeng.MSLback.domain.entity.User#1] - no Session 에러 발생
     * @param id
     */
//    public Post getPost(Long id) {
//        System.out.println("check");
//        Post post = findOne(id);
//        return post;
//    }

    /**
     * 게시글 상세 조회
     * 로그인 상태로 접근
     */
    public PostDetailDto getPost(Long postId, Long userId) {

        Post findPost = postRepository.findByPostIdAndPostFetchJoinUser(postId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 게시글입니다."));
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));
        // TODO: 쿼리 수를 줄일 수 있을까? 다시 한 번 생각하기
        log.info("state 검사");
        boolean state = postLikeRepository.existsPostLikeByUser(findUser);
        /*
        게시글 상세조회를 했을 때, 내가 작성자를 팔로우 했는지 안 했는지 확인하는 쿼리를 날리는게 좋을까? 다른 방법이 있을까?
         */
        /*
        팔로우를 좀 더 효율적으로 갖고올 수 없을까? 쿼리가 너무 많이 나감
         */
        Optional<Follow> followState = followRepository.findByFollowerIdAndFollowingId(userId, findPost.getUser().getId());
        System.out.println("followState.isPresent() = " + followState.isPresent());
        log.info("state 검사");
        PostDetailDto dto = new PostDetailDto(findPost, state, followState.isPresent(), userId);
        return dto;
    }
    /**
     * 게시글 상세 조회
     * "비로그인" 상태로 접근
     */
    public PostDetailDto noLoginAndGetPost(Long postId) {

        Post findPost = postRepository.findByPostIdAndPostFetchJoinUser(postId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 게시글입니다."));
        PostDetailDto dto = new PostDetailDto(findPost, false);
        return dto;
    }

    /**
     * 게시글 전체 조회
     */
    public List<Post> getAllPost() {
        List<Post> post = postRepository.findAll();
        return post;
    }

    public List<PostListResponseDto> testAllPost() {
        List<Post> post = postRepository.findAll();
        return post.stream().map(p -> new PostListResponseDto(p, 1, 1)).collect(Collectors.toList());
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void deletePost(Long postId, Long myId) {

        Post post = findOne(postId);
        if (post.getUser().getId() != myId) {
            throw new IllegalStateException("내가 등록한 글이 아니기 때문에 삭제 불가능합니다");
        }
        System.out.println("log.info(\"deletePost, postId = {}\", postId);");
        postRepository.deleteById(postId);
        List<HashTag> hashTag = post.getHashTag();
        for (HashTag hashtag : hashTag) {
            hashTagService.deleteTags(hashtag.getTag());
        }
    }





    /**
     * post_id를 이용해서 게시글 불러오기
     * @param id
     */
    private Post findOne(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalStateException("게시글을 찾을 수 없습니다.")
                );

        return post;
    }
}
