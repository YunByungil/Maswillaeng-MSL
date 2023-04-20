package Maswillaeng.MSLback.test;

import Maswillaeng.MSLback.domain.entity.Post;
import Maswillaeng.MSLback.domain.entity.User;
import Maswillaeng.MSLback.domain.enums.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class TestDto {

    private String thumbnail;
    private String title;
    private String content;
    private Category category;
    //    private Set<String> tag = new LinkedHashSet<>();
    private List<String> tag = new ArrayList<>();
    private List<String> image = new ArrayList<>();
    public Post toEntity(User user) {
        return Post.builder()
                .user(user)
                .thumbnail(thumbnail)
                .title(title)
                .content(content)
                .category(category)
                .build();
    }
}
