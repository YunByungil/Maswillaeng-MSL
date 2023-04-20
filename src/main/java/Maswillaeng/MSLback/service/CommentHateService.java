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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentHateService {

    private final CommentHateRepository commentHateRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    public CommentLikeAndHateResponseDto hateComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("해당 댓글이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("해당 회원이 존재하지 않습니다."));
        Optional<CommentHate> findCommentHate = commentHateRepository.findByCommentIdAndUserId(commentId, userId);
        if (findCommentHate.isPresent()) {
            throw new IllegalStateException("해당 댓글에 이미 싫어요를 누르셨습니다.");
        }


        /**
         * 싫어요 눌렀을 떄 좋아요를 누른 상태면, 좋아요는 사라지고 싫어요가 올라간다
         */
        Optional<CommentLike> findCommentLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);
        if (findCommentLike.isPresent()) {
            commentLikeRepository.delete(findCommentLike.get());
        }

        CommentHate commentHate = CommentHate.builder()
                .user(user)
                .comment(comment)
                .build();
        commentHateRepository.save(commentHate);

        long likeCount = commentLikeRepository.countByCommentId(commentId);
        long hateCount = commentHateRepository.countByCommentId(commentId);

        return new CommentLikeAndHateResponseDto(userId, comment.getUser().getId(), commentId, likeCount, hateCount);
    }

    public CommentLikeAndHateResponseDto unHateComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("해당 댓글이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("해당 회원이 존재하지 않습니다."));

        CommentHate commentHate = commentHateRepository.findByCommentIdAndUserId(commentId, userId)
                .orElseThrow(() -> new IllegalStateException("해당 댓글에 싫어요를 누르지 않았습니다."));

        commentHateRepository.delete(commentHate);
        long likeCount = commentLikeRepository.countByCommentId(commentId);
        long hateCount = commentHateRepository.countByCommentId(commentId);

        return new CommentLikeAndHateResponseDto(userId, comment.getUser().getId(), commentId, likeCount, hateCount);
    }
}
