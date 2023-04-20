package Maswillaeng.MSLback.test;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class WebController {

    private final S3Uploader s3Uploader;

    @GetMapping("/tests3")
    public String index() {
        return "test";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("data") MultipartFile multipartFile,
                         Authentication authentication) throws IOException {
        System.out.println("multipartFile.getOriginalFilename() = " + multipartFile.getOriginalFilename());
        Long userId = Long.parseLong(authentication.getName());
        return s3Uploader.upload(multipartFile, "static", userId);
    }
    /*
    테스트
     */
    @PostMapping("/upload2")
    @ResponseBody
    public ResponseEntity<List<String>> upload2(@RequestParam("data") List<MultipartFile> multipartFile,
                                  Authentication authentication) throws IOException {
//        System.out.println("multipartFile.getOriginalFilename() = " + multipartFile.getOriginalFilename());
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(s3Uploader.upload2(multipartFile, "static", userId));
    }
}
