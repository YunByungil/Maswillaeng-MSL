package Maswillaeng.MSLback.controller;

import Maswillaeng.MSLback.domain.entity.User;
import Maswillaeng.MSLback.dto.user.reponse.*;
import Maswillaeng.MSLback.dto.user.request.UserLoginRequestDto;
import Maswillaeng.MSLback.dto.user.request.UserJoinDTO;
import Maswillaeng.MSLback.dto.user.request.UserListDTO;
import Maswillaeng.MSLback.dto.user.request.UserUpdateDTO;
import Maswillaeng.MSLback.service.UserService;
import Maswillaeng.MSLback.utils.CookieUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final CookieUtil cookieUtil;


    /**
     * 모든 회원 조회 (프론트에서 유효성 검사)
     * TODO: Response 수정
     */
    @GetMapping("/user-list")
    public Result memberList() {
        List<UserListDTO> allUserList = userService.findAll();
        return new Result(allUserList);
    }

    /**
     * 이메일 중복 검사 api
     */
    @GetMapping("/email-duplicate")
    public ResponseEntity checkEmailDuplicate(String email) {
        userService.checkEmailDuplicate(email);
        return ResponseEntity.ok().body("이미 존재하는 Email입니다.");
    }

    /**
     * 닉네임 중복 검사 api
     */
    @GetMapping("/nickname-duplicate")
    public ResponseEntity checkNicknameDuplicate(String nickname) {
        userService.checkNicknameDuplicate(nickname);
        return ResponseEntity.ok().body("이미 존재하는 Nickname입니다.");
    }

    /**
     * 핸드폰 번호 중복 검사 api
     */
    @GetMapping("/phone-duplicate")
    public ResponseEntity checkPhoneNumberDuplicate(String phoneNumber) {
        userService.checkPhoneNumberDuplicate(phoneNumber);
        return ResponseEntity.ok().body("이미 존재하는 PhoneNumber입니다.");
    }

    /**
     * 회원 조회할 때, 반환 값을 어떻게 해야될지,
     * res에는 분명 배열 형태인데 dto넘기는 건데 왜 배열이지?
     */
//    @GetMapping("/user")
//    public ResponseEntity<Object> member(Authentication authentication) {
//        User user = userService.findOne(Long.parseLong(authentication.getName()));
//        UserListDTO userListDTO = new UserListDTO(user);
//        return ResponseEntity.ok().body(userListDTO);
//    }

    /**
     * 유저 조회 테스트 (마이 페이지라고 생각하자 일단은)
     */
    @GetMapping("/user/{userId}")
    public LoginResponseDto testMember(@PathVariable("userId") Long userId, Authentication authentication) {
        /*
        로그인 상태가 아닐 때, (비로그인)
         */
        if (authentication == null) {
            UserDetailResponseDto dto = userService.noLoginAndGetMember(userId);
            return new LoginResponseDto(HttpStatus.OK.value(), dto);
        }
        /*
        내가 로그인 상태일 때,
         */
        Long myId = Long.parseLong(authentication.getName());
//        UserListDTO userListDTO = new UserListDTO(user);
        UserDetailResponseDto dto = userService.testMember(userId, myId);
        return new LoginResponseDto(HttpStatus.OK.value(), dto);
    }

    /**
     * 회원 수정
     * @param authentication
     * @param dto
     * @return
     */
    @PutMapping("/user")
    public ResponseEntity updateMember(Authentication authentication, @RequestBody UserUpdateDTO dto) {
        Long userId = Long.parseLong(authentication.getName());
        userService.update(userId, dto);
        return ResponseEntity.ok().body("수정 완료!");
    }

    @PostMapping("/test")
    public ResponseEntity<String> test(Authentication authentication) {
        log.info("userId = {}", authentication.getName());
//        log.info("co = {}", co);
        return ResponseEntity.ok().body("님의 test");
    }

    @GetMapping("/test2")
    public String test2() {
        return "okTest2";
    }

    /**
     * login
     */
//    @GetMapping("/loginTest")
//    public ResponseEntity<?> login(@RequestBody UserLoginRequestDto dto) {
//        // TODO: 토큰 정보 어떻게 뿌릴지?
//        //https://seob.dev/posts/%EB%B8%8C%EB%9D%BC%EC%9A%B0%EC%A0%80-%EC%BF%A0%ED%82%A4%EC%99%80-SameSite-%EC%86%8D%EC%84%B1/
//
//        UserLoginResponseDto userLoginResponseDto = userService.login(dto);
//
//        ResponseCookie accessToken = cookieUtil.createAccessCookieToken(userLoginResponseDto);
//
//        ResponseCookie refreshToken = cookieUtil.createRefreshCookieToken(userLoginResponseDto);
//
//        LoginResultResponse l = getLoginResultResponse(userLoginResponseDto);
//
//        return ResponseEntity.ok()
//                .header("Set-Cookie", accessToken.toString())
//                .header("Set-Cookie", refreshToken.toString())
//                .body(new LoginResponseDto(HttpStatus.OK.value(), l));
//    }

    private LoginResultResponse getLoginResultResponse(UserLoginResponseDto userLoginResponseDto) {
        LoginResultResponse l = LoginResultResponse
                .builder()
                .nickname(userLoginResponseDto.getNickname())
                .userImage(userLoginResponseDto.getUserImage())
                .build();
        return l;
    }

    /**
     * 회원 가입
     * @param userJoinDTO
     * @return
     */
    @PostMapping("/sign")
    public Result join(@RequestBody UserJoinDTO userJoinDTO) {
        log.info("userJoinDTO = {}", userJoinDTO);
        userService.join(userJoinDTO);
        return new Result(HttpStatus.OK.value());
    }

    @GetMapping("/token")
    public ResponseEntity reissue(@CookieValue("refreshToken") String to, Authentication authentication) {
        System.out.println("\"gdgd\" = " + "gdgd");

        System.out.println("authentication = " + to.toString());
        TokenResponse token = userService.reissueAccessToken(to);

        ResponseCookie cookie = cookieUtil.createCookie(token.getAccessToken());
        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body("토큰 발급 완료");
    }

    /**
     * 로그아웃
     * 유저 정보 불러와서, 1. DB에 담겨있는 리프레쉬 토큰 삭제
     * 2. 헤더에 있는 쿠키 정보 삭제
     */
//    @PostMapping("/logout")
    public ResponseEntity logout(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        log.info("userId = {}", userId);
        userService.deleteRefreshToken(userId);
        // TODO: 변수명 수정하기
        ResponseCookie cookie = cookieUtil.deleteAccessCookieToken();
        ResponseCookie cookie2 = cookieUtil.deleteRefreshCookieToken();
        log.info("logout 완료");

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .header("Set-Cookie", cookie2.toString())
                .body("gd");
    }


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T code;
    }
}
