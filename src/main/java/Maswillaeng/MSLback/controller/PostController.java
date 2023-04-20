package Maswillaeng.MSLback.controller;

import Maswillaeng.MSLback.domain.entity.Post;
import Maswillaeng.MSLback.domain.enums.Category;
import Maswillaeng.MSLback.domain.repository.post.query.PostSearchRepository;
import Maswillaeng.MSLback.domain.repository.post.query.PostTestRepository;
import Maswillaeng.MSLback.dto.post.reponse.*;
import Maswillaeng.MSLback.dto.post.request.*;
import Maswillaeng.MSLback.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final PostTestRepository postTestRepository;
    private final PostSearchRepository postSearchRepository;
    @GetMapping()
    public String index() {
        return "index";
    }

    /**
     * 글쓰기
     * @param authentication
     * @param postRequestDto
     * TODO: 카테고리
     * @return
     */
    @PostMapping("/post")
    public PostResponse addPost(Authentication authentication, @RequestBody PostRequestDto postRequestDto) {
        // TODO: @Valid

        log.info(authentication.getName());
        System.out.println("id = " + authentication.getName());
        Long id = Long.parseLong(authentication.getName());
        log.info("content = {}", postRequestDto.getContent());
        postService.addPost(id, postRequestDto);
        /*
        테스트
         */

        return new PostResponse(HttpStatus.OK.value());
    }


    /**
     * 게시글 수정
     * @param postId
     * @param dto
     * @return
     */
    @PutMapping("/post/{postId}")
    public PostResponse updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequestDto dto) {
        postService.updatePost(postId, dto);
        return new PostResponse(HttpStatus.OK.value());
    }

    /**
     * 게시글 상세 조회
     */
    @GetMapping("/post/{postId}")
    public PostDetailResponse getPost(@PathVariable Long postId, Authentication authentication) {
        if (authentication == null) {
            PostDetailDto dto = postService.noLoginAndGetPost(postId);
            return new PostDetailResponse(HttpStatus.OK.value(), dto);
        }
        Long userId = Long.parseLong(authentication.getName());
        PostDetailDto dto = postService.getPost(postId, userId);
//        PostDetailDto dto = new PostDetailDto(post);
        return new PostDetailResponse(HttpStatus.OK.value(), dto);
    }

    /**
     * 게시글 전체 조회
     * TODO: 이것도 Post로 받는게 아니라, Dto로 받아야 된다.
     */
//    @GetMapping("/post/page")
//    public PostListResponse getAllPost() {
//        List<Post> allPost = postService.getAllPost();
//        List<PostListDto> collect = allPost.stream()
//                .map(p -> new PostListDto(p.getId(), p.getUser().getNickname(), p.getThumbnail(), p.getTitle()))
//                .collect(Collectors.toList());
//
//        return new PostListResponse(collect.size(), HttpStatus.OK.value(), collect);
//    }

    /**
     * 연습용
     */
    @GetMapping("/post/page")
    public PostListResponse getAllPost(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                       @RequestParam(value = "limit", defaultValue = "100") int limit) {
//        List<PostListResponseDto> dto = postService.testAllPost();
        List<PostListResponseDto> dto2 = postTestRepository.test(offset, limit);
//        List<Post> dto2 = postTestRepository.fetchJoin();
        return new PostListResponse(dto2.size(), HttpStatus.OK.value(), dto2);
    }
//    @GetMapping("/post/page")
//    public PostListResponse getAllPost() {
//        List<PostListResponseDto> dto = postService.testAllPost();
//
//        return new PostListResponse(dto.size(), HttpStatus.OK.value(), dto);
//    }

    /**
     * 카테고리별 리스트 조회
     */
    @GetMapping("/post/list")
    public PostListResponse getPostList(String category, Pageable pageable) {
        System.out.println("category = " + category);
        Page<PostListResponseDto> postList = postSearchRepository.getPostList(category, pageable);
        return new PostListResponse(postList.getSize(), HttpStatus.OK.value(), postList);
    }
    /*
    테스트
    카테고리별 리스트 조회 Slice 방식
     */
    @GetMapping("/post/list2")
    public PostListResponse sliceList(String category, Pageable pageable) {
        System.out.println("category = " + category);
        Slice<PostListResponseDto> postList = postSearchRepository.sliceList(category, pageable);
        return new PostListResponse(postList.getSize(), HttpStatus.OK.value(), postList);
    }


    /**
     * 게시글 삭제
     */
    @DeleteMapping("/post/{postId}")
    public PostResponse deletePost(Authentication authentication, @PathVariable Long postId) {
        /*
        대체 왜 /post/{postId}로 하면 1번 게시글이 삭제가 안 될까..
         */
        log.info("== deletePost Method ==");
        log.info("deletePost Method");
        log.info("deletePost, postId = {}", postId);
        log.info("user = {}", authentication.getName());
        log.info("user = {}", authentication.getName());
        log.info("user = {}", authentication.getName());
        log.info("user = {}", authentication.getName());
        log.info("userId = {}", Long.parseLong(authentication.getName()));
        Long myId = Long.parseLong(authentication.getName());
        postService.deletePost(postId, myId);
        return new PostResponse(HttpStatus.OK.value());
    }

    /**
     * 게시글 검색
     */
    @GetMapping("/post/search")
    public PostListResponse searchPost(PostSearchCondition condition, Pageable pageable) {
        Page<SearchTestDto> searchResult = postSearchRepository.testV2(condition, pageable);
        System.out.println("searchResult.getSize() = " + searchResult.getSize());
        for (SearchTestDto searchTestDto : searchResult) {
            System.out.println("searchTestDto = " + searchTestDto);
        }
        return new PostListResponse(searchResult.getSize(), HttpStatus.OK.value(), searchResult);
    }
}
