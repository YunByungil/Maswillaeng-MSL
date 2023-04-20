package Maswillaeng.MSLback.controller;

import Maswillaeng.MSLback.dto.post.reponse.PostLikeResponseDto;
import Maswillaeng.MSLback.service.PostLikeService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostLikeController {

    private final PostLikeService postLikeService;

    /**
     * 게시글 좋아요
     * @param postId
     * @param authentication
     * @return
     */
    @PostMapping("/{postId}/like")
    public PostLikeResponse likePost(@PathVariable("postId") Long postId, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        PostLikeResponseDto dto = postLikeService.likePost(postId, userId);
        return new PostLikeResponse(HttpStatus.OK.value(), "좋아요!", dto);
    }

    /**
     * 좋아요 취소
     */
    @DeleteMapping("/{postId}/like")
    public PostLikeResponse unlikePost(@PathVariable("postId") Long postId, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        PostLikeResponseDto dto = postLikeService.unlikePost(postId, userId);
        return new PostLikeResponse(HttpStatus.OK.value(), "좋아요 취소!", dto);
    }

    @Getter
    @AllArgsConstructor
    static class PostLikeResponse<T> {

        private int code;
        private String message;
        private T result;
    }
}
