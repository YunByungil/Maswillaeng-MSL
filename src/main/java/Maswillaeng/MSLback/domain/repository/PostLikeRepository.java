package Maswillaeng.MSLback.domain.repository;

import Maswillaeng.MSLback.domain.entity.PostLike;
import Maswillaeng.MSLback.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);
    long countByPostId(Long postId);
    boolean existsPostLikeByUser(User user);

    @Query("select p " +
            "from PostLike p " +
            "join fetch p.post " +
            "where p.user.id = :userId")
    List<PostLike> findPostLikeByUserIdAndFetchJoinPost(@Param("userId") Long userId);
}
