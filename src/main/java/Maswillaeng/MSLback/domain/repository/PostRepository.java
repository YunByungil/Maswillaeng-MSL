package Maswillaeng.MSLback.domain.repository;

import Maswillaeng.MSLback.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 지정한 유저가 쓴 글만 불러오는 메서드 (미완성)
     */
    List<Post> findByUserId(Long userId);

//    /**
//     * 게시글 상세보기 (데이터 뻥튀기)
//     * @param postId
//     */
//    @Query("select p " +
//            "from Post p " +
//            "join fetch p.user " +
//            "join fetch p.hashTag ht " +
//            "join fetch ht.tag t " +
//            "where p.id =:postId")
//    Optional<Post> findByPostIdAndPostFetchJoinUser(@Param("postId") Long postId);
    /**
     * 게시글 상세보기
     * @param postId
     */
//    @Query("select p " +
//            "from Post p " +
//            "join fetch p.user " +
//            "join fetch p.hashTag ht " +
//            "join fetch ht.tag t " +
//            "where p.id =:postId")
//    Optional<Post> findByPostIdAndPostFetchJoinUser(@Param("postId") Long postId);
    @Query("select p " +
            "from Post p " +
            "join fetch p.user " +
            "left join fetch p.hashTag ht " +
            "left join fetch ht.tag t " +
            "where p.id =:postId")
    Optional<Post> findByPostIdAndPostFetchJoinUser(@Param("postId") Long postId);
}
