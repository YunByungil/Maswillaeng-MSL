package Maswillaeng.MSLback.service;

import Maswillaeng.MSLback.domain.entity.HashTag;
import Maswillaeng.MSLback.domain.entity.Post;
import Maswillaeng.MSLback.domain.entity.Tag;
import Maswillaeng.MSLback.domain.repository.HashTagRepository;
import Maswillaeng.MSLback.domain.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HashTagService {

    private final HashTagRepository hashTagRepository;
    private final TagRepository tagRepository;

    public void addHashTag(Post post, List<String> tags) {
        for (String tag : tags) {
            Tag t = findTags(tag);
            hashTagRepository.save(new HashTag(t, post));
        }
    }

    public void updateHashTag(Post post, List<String> tags) {
//        for (String tag : tags) {
//            Tag t = findTags(tag);
//            hashTagRepository.save(new HashTag(t, post));
//        }
        // TODO: 업데이트 확인 ->
        List<HashTag> hashTag = post.getHashTag();
        for (HashTag hashtag : hashTag) {
            hashTagRepository.delete(hashtag);
            deleteTags(hashtag.getTag());
        }
    }

    private Tag findTags(String tag) {
        return tagRepository.findByName(tag)
                .orElseGet(() -> tagRepository.save(new Tag(tag)));


    }
    
    public void deleteTags(Tag tag) {
        if (!hashTagRepository.existsByTag(tag)) {
            System.out.println("\"\" = " + "존재한다");
            tagRepository.deleteById(tag.getId());
        }
    }
}
