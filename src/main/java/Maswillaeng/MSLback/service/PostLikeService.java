package Maswillaeng.MSLback.service;

import Maswillaeng.MSLback.domain.entity.Post;
import Maswillaeng.MSLback.domain.entity.PostLike;
import Maswillaeng.MSLback.domain.entity.User;
import Maswillaeng.MSLback.domain.repository.PostLikeRepository;
import Maswillaeng.MSLback.domain.repository.PostRepository;
import Maswillaeng.MSLback.domain.repository.UserRepository;
import Maswillaeng.MSLback.dto.post.reponse.PostLikeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 게시글 좋아요
     * @param postId
     * @param userId
     */
    public PostLikeResponseDto likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("해당 게시글이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("해당 회원이 존재하지 않습니다."));

        Optional<PostLike> findPost = postLikeRepository.findByPostIdAndUserId(postId, userId); // 중복 좋아요 검증 로직
        if (findPost.isEmpty()) {
            PostLike postLike = PostLike.builder()
                    .post(post)
                    .user(user)
                    .build();

            postLikeRepository.save(postLike);
            /**
             * 쿼리를 한 번 더 날려도 될까?
             */
            Long likeCount = postLikeRepository.countByPostId(postId);
            return new PostLikeResponseDto(userId, post.getId(), postId, likeCount);
        } else {
            throw new IllegalStateException("해당 게시글에 이미 좋아요를 누르셨습니다.");
        }
    }


    public PostLikeResponseDto unlikePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("해당 게시글이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("해당 회원이 존재하지 않습니다."));

        PostLike postLike = postLikeRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new IllegalStateException("해당 게시글에 좋아요를 누르지 않았습니다."));

//        PostLike postLike = PostLike.builder()
//                .post(post)
//                .user(user)
//                .build();

        postLikeRepository.delete(postLike);
        Long likeCount = postLikeRepository.countByPostId(postId);
        return new PostLikeResponseDto(userId, post.getId(), postId, likeCount);
    }
}
