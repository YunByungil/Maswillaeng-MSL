package Maswillaeng.MSLback.service;

import Maswillaeng.MSLback.domain.entity.Comment;
import Maswillaeng.MSLback.domain.entity.CommentHate;
import Maswillaeng.MSLback.domain.entity.CommentLike;
import Maswillaeng.MSLback.domain.entity.User;
import Maswillaeng.MSLback.domain.repository.CommentHateRepository;
import Maswillaeng.MSLback.domain.repository.CommentLikeRepository;
import Maswillaeng.MSLback.domain.repository.CommentRepository;
import Maswillaeng.MSLback.domain.repository.UserRepository;
import Maswillaeng.MSLback.dto.comment.response.CommentLikeAndHateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentHateRepository commentHateRepository;

    /**
     * 댓글 좋아요
     */
    public CommentLikeAndHateResponseDto likeComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("해당 댓글이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("해당 회원이 존재하지 않습니다."));

        Optional<CommentLike> findComment = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);
        if (findComment.isPresent()) {
            throw new IllegalStateException("해당 댓글에 이미 좋아요를 누르셨습니다.");
        }

        /**
         * 좋아요 눌렀을 떄 싫어요를 누른 상태면, 싫어요는 사라지고 좋아요가 올라간다
         * TODO: 성능 개선하기
         */
        Optional<CommentHate> findCommentHate = commentHateRepository.findByCommentIdAndUserId(commentId, userId);
        if (findCommentHate.isPresent()) {
            commentHateRepository.delete(findCommentHate.get());
        }

        CommentLike commentLike = CommentLike.builder()
                .comment(comment)
                .user(user)
                .build();

        commentLikeRepository.save(commentLike);
        long likeCount = commentLikeRepository.countByCommentId(commentId);
        long hateCount = commentHateRepository.countByCommentId(commentId);

        return new CommentLikeAndHateResponseDto(userId, comment.getUser().getId(), commentId, likeCount, hateCount);
    }

    public CommentLikeAndHateResponseDto unlikeComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("해당 댓글이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("해당 회원이 존재하지 않습니다."));

        CommentLike commentLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId)
                .orElseThrow(() -> new IllegalStateException("해당 댓글에 좋아요를 누르지 않았습니다."));

        // TODO: 코멘트, 유저, 좋아요 여부, 좋아요 개수, 싫어요 개수 쿼리가 너무 많이 나가는데 어떻게 줄일까?
        commentLikeRepository.delete(commentLike);
        long likeCount = commentLikeRepository.countByCommentId(commentId);
        long hateCount = commentHateRepository.countByCommentId(commentId);

        return new CommentLikeAndHateResponseDto(userId, comment.getUser().getId(), commentId, likeCount, hateCount);
    }
}
