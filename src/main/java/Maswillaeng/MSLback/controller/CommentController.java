package Maswillaeng.MSLback.controller;

import Maswillaeng.MSLback.dto.comment.request.CommentRequestDto;
import Maswillaeng.MSLback.dto.comment.request.CommentUpdateDto;
import Maswillaeng.MSLback.dto.comment.request.ReCommentRequestDto;
import Maswillaeng.MSLback.dto.comment.response.ChildCommentResponse;
import Maswillaeng.MSLback.dto.comment.response.CommentResponseDto;
import Maswillaeng.MSLback.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성
     */
    @PostMapping("/post/{postId}/comment")
    public ResponseEntity addComment(@PathVariable("postId") Long postId,
                                     @RequestBody CommentRequestDto dto,
                                     Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        commentService.addComment(postId, userId, dto);
        return ResponseEntity.ok().body(dto);
    }

    /**
     * 대댓글 작성
     * @param postId
     * @param commentId
     * @param dto
     * @param authentication
     * @return
     */
    @PostMapping("/post/{postId}/{commentId}/comment")
    public ResponseEntity addReComment(@PathVariable("postId") Long postId,
                                       @PathVariable("commentId") Long commentId,
                                       @RequestBody ReCommentRequestDto dto,
                                       Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        commentService.addReComment(postId, userId, commentId, dto);
        return ResponseEntity.ok().body(dto);
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/post/{postId}/{commentId}/comment")
    public ResponseEntity updateComment(@PathVariable("postId") Long postId,
                                        @PathVariable("commentId") Long commentId,
                                        @RequestBody CommentUpdateDto dto,
                                        Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        commentService.updateComment(postId, userId, commentId, dto);
        return ResponseEntity.ok().body(dto);
    }

//    // TODO
//    @PutMapping("/post/{postId}/{commentId}/comment")
//    public ResponseEntity updateReComment(@PathVariable("postId") Long postId,
//                                          @PathVariable("commentId") Long commentId,
//                                          @RequestBody ReCommentRequestDto dto,
//                                          Authentication authentication) {
//         Long userId = Long.parseLong(authentication.getName());
//         return ResponseEntity.ok().body(dto);
//    }

    /**
     * 댓글 삭제
     * @param postId 게시글 id
     * @param commentId 댓글 id
     * @param authentication 회원정보
     * @return
     */
    @DeleteMapping("/post/{postId}/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("postId") Long postId,
                                        @PathVariable("commentId") Long commentId,
                                        Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        commentService.deleteComment(postId, userId, commentId);

        return ResponseEntity.ok().body("댓글 삭제 완료");
    }

    /**
     * 대댓글 불러오기
     */
    @GetMapping("/post/{postId}/{commentId}/comment")
    public ChildCommentResponse getChildComment(@PathVariable("postId") Long postId,
                                                @PathVariable("commentId") Long commentId,
                                                Authentication authentication) {
        if (authentication == null) {
            List<CommentResponseDto> childComment = commentService.getChildComment(postId, commentId);
            return new ChildCommentResponse(childComment);
        }
        Long userId = Long.parseLong(authentication.getName());
        List<CommentResponseDto> childComment = commentService.getChildComment(postId, commentId, userId);
        System.out.println("\"childComment\" = " + "childComment");
        return new ChildCommentResponse(childComment);

    }


}
