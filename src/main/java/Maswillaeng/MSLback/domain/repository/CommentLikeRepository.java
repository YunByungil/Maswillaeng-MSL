package Maswillaeng.MSLback.domain.repository;

import Maswillaeng.MSLback.domain.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByCommentIdAndUserId(Long commentId, Long userId);

    long countByCommentId(Long commentId);
}
