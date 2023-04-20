package Maswillaeng.MSLback.domain.repository;

import Maswillaeng.MSLback.domain.entity.CommentHate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentHateRepository extends JpaRepository<CommentHate, Long> {

    Optional<CommentHate> findByCommentIdAndUserId(Long commentId, Long userId);

    long countByCommentId(Long commentId);
}
