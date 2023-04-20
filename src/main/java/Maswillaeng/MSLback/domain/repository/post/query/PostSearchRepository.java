package Maswillaeng.MSLback.domain.repository.post.query;

import Maswillaeng.MSLback.domain.entity.Post;
import Maswillaeng.MSLback.domain.enums.Category;
import Maswillaeng.MSLback.dto.post.reponse.PostListResponseDto;
import Maswillaeng.MSLback.dto.post.reponse.SearchTestDto;
import Maswillaeng.MSLback.dto.post.request.PostSearchCondition;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static Maswillaeng.MSLback.domain.entity.QComment.*;
import static Maswillaeng.MSLback.domain.entity.QPost.*;
import static Maswillaeng.MSLback.domain.entity.QUser.user;
import static com.querydsl.core.types.dsl.Expressions.list;
import static com.querydsl.core.types.dsl.Expressions.stringTemplate;
import static org.springframework.util.StringUtils.*;

@Repository
@Transactional(readOnly = true)
public class PostSearchRepository {

    private final JPAQueryFactory queryFactory;

    public PostSearchRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<SearchTestDto> testV2(PostSearchCondition condition, Pageable pageable) {
        JPAQuery<Post> test = queryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());



        List<Post> posts2 = test
                .leftJoin(post.comment, comment)
                .where(
                        commentContentContain(condition.getCommentContent()),
                        commentWriterContain(condition.getCommentWriter()),
                        postWriterContain(condition.getPostWriter()),
                        titleContain(condition.getTitle()),
                        postContentOrTitleContain(condition.getPostContent())
                )
                .fetch();

        List<SearchTestDto> content = posts2.stream()
                .map(p -> new SearchTestDto(p, p.getComment().size(), p.getPostLike().size()))
                .collect(Collectors.toList());

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        commentContentContain(condition.getCommentContent()),
                        commentWriterContain(condition.getCommentWriter()),
                        postWriterContain(condition.getPostWriter()),
                        titleContain(condition.getTitle()),
                        postContentOrTitleContain(condition.getPostContent())
                );

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchOne());
    }

    /**
     * 카테고리별 게시글 단순 조회
     */
    public Page<PostListResponseDto> getPostList(String category, Pageable pageable) {
        JPAQuery<Post> query = queryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .where(
                        categoryEqual(category)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<PostListResponseDto> content = query.stream()
                .map(p -> new PostListResponseDto(p, p.getComment().size(), p.getPostLike().size()))
                .collect(Collectors.toList());

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where();

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchOne());
    }
    /*
    테스트
     */
    public Slice<PostListResponseDto> sliceList(String category, Pageable pageable) {
        JPAQuery<Post> query = queryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .where(
                        categoryEqual(category)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        List<PostListResponseDto> content = query.stream()
                .map(p -> new PostListResponseDto(p, p.getComment().size(), p.getPostLike().size()))
                .collect(Collectors.toList());

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where();

//        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchOne());
        return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanExpression categoryEqual(String category) {
        if (!hasText(category.toString())) {
            return null;
        }

        return post.category.eq(Category.valueOf(category));
    }

    private BooleanExpression postWriterContain(String postWriter) {
        if (!hasText(postWriter)) {
            return null;
        }
        return user.nickname.contains(postWriter);
    }

    private BooleanExpression titleContain(String title) {
        if (!hasText(title)) {
            return null;
        }
        return post.title.contains(title);
    }

    private BooleanExpression postContentOrTitleContain(String postContent) {
        if (!hasText(postContent)) {
            return null;
        }
        return post.content.contains(postContent).or(post.title.contains(postContent));
    }

    private BooleanExpression commentContentContain(String commentContent) {
        if (!hasText(commentContent)) {
            return null;
        }
        return comment.content.contains(commentContent);
    }

    private BooleanExpression commentWriterContain(String commentWriter) {
        if (!hasText(commentWriter)) {
            return null;
        }
        return comment.user.nickname.contains(commentWriter);
    }

}
