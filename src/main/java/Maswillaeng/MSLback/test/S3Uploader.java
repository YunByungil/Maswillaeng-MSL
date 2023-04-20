package Maswillaeng.MSLback.test;

import Maswillaeng.MSLback.domain.entity.Post;
import Maswillaeng.MSLback.domain.entity.User;
import Maswillaeng.MSLback.service.PostService;
import Maswillaeng.MSLback.service.UserService;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;
    private final UserService userService;
    private final PostService postService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName, Long userId) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalStateException("Multipart -> File 전환 실패"));

        return upload(uploadFile, dirName, userId);
    }

    private String upload(File uploadFile, String dirName, Long userId) {
        System.out.println("dirName = " + dirName);
        UUID uuid = UUID.randomUUID();
        String fileName = dirName + "/" + uuid.toString() + "_" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }


    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
    /*
    여기부터 테스트 다중 파일
     */
    public List<String> upload2(List<MultipartFile> multipartFile, String dirName, Long userId) throws IOException {
        List<String> list = new ArrayList<>();
        if (!multipartFile.isEmpty()) {
            for (MultipartFile file : multipartFile) {
                File uploadFile = convert(file)
                        .orElseThrow(() -> new IllegalStateException("Multipart -> File 전환 실패"));
                list.add(upload2(uploadFile, dirName, userId));
            }
        }
        User user = userService.findOne(userId);
        TestDto dto = new TestDto();
        Post post = dto.toEntity(user);
        return list;
    }

    private String upload2(File uploadFile, String dirName, Long userId) {
        System.out.println("dirName = " + dirName);
        UUID uuid = UUID.randomUUID();
        String fileName = dirName + "/" + uuid.toString() + "_" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        System.out.println("uploadImageUrl = " + uploadImageUrl);
//        User findUser = userService.findOne(userId);
//        findUser.setUserImage(uploadImageUrl);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }
}

