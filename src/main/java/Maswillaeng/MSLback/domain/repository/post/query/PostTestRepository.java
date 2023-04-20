package Maswillaeng.MSLback.domain.repository.post.query;

import Maswillaeng.MSLback.domain.entity.Post;
import Maswillaeng.MSLback.dto.post.reponse.PostListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Transactional
public class PostTestRepository {

    private final EntityManager em;


    public List<PostListResponseDto> test(int offset, int limit) {
        List<Post> dto = findAll(offset, limit);
        List<PostListResponseDto> dto2 = dto.stream()
                .map(p -> new PostListResponseDto(p, p.getComment().size(), p.getPostLike().size()))
                .collect(Collectors.toList());
        return dto2;
    }
    public List<Post> findAll(int offset, int limit) {
        return em.createQuery(
                "select p from Post p" +
                        " join fetch p.user u", Post.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
    public List<Post> fetchJoin() {
        return em.createQuery(
                "select p from Post p" +
                        " join fetch p.user u" +
                        " join fetch p.comment c" +
                        " join fetch p.postLike l", Post.class)
                .getResultList();
    }
}
