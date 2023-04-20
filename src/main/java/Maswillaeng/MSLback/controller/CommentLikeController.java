package Maswillaeng.MSLback.controller;

import Maswillaeng.MSLback.dto.comment.response.CommentLikeAndHateResponse;
import Maswillaeng.MSLback.dto.comment.response.CommentLikeAndHateResponseDto;
import Maswillaeng.MSLback.service.CommentHateService;
import Maswillaeng.MSLback.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;
    private final CommentHateService commentHateService;
    /**
     * 댓글 좋아요
     */
    @PostMapping("/{commentId}/commentlike")
    public CommentLikeAndHateResponse likeComment(@PathVariable("commentId") Long commentId, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        CommentLikeAndHateResponseDto dto = commentLikeService.likeComment(commentId, userId);
        return new CommentLikeAndHateResponse(HttpStatus.OK.value(), "좋아요!", dto);
    }

    @DeleteMapping("/{commentId}/commentlike")
    public CommentLikeAndHateResponse unlikeComment(@PathVariable("commentId") Long commentId, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        CommentLikeAndHateResponseDto dto = commentLikeService.unlikeComment(commentId, userId);
        return new CommentLikeAndHateResponse(HttpStatus.OK.value(), "댓글 좋아요 취소!", dto);
    }

    /**
     * 댓글 싫어요
     */
    @PostMapping("/{commentId}/commenthate")
    public CommentLikeAndHateResponse hateComment(@PathVariable("commentId") Long commentId, Authentication authentication) {
         Long userId = Long.parseLong(authentication.getName());
        CommentLikeAndHateResponseDto dto = commentHateService.hateComment(commentId, userId);
        return new CommentLikeAndHateResponse(HttpStatus.OK.value(), "댓글 싫어요!", dto);
    }

    @DeleteMapping("/{commentId}/commenthate")
    public CommentLikeAndHateResponse unHateComment(@PathVariable("commentId") Long commentId, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        CommentLikeAndHateResponseDto dto = commentHateService.unHateComment(commentId, userId);
        return new CommentLikeAndHateResponse(HttpStatus.OK.value(), "댓글 싫어요 취소!", dto);
    }
}
