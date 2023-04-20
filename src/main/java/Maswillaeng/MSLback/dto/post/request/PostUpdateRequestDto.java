package Maswillaeng.MSLback.dto.post.request;

import Maswillaeng.MSLback.domain.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateRequestDto {

    private Long id;
    private String thumbnail;
    private String title;
    private String content;
    private Category category;
    private List<String> tag = new ArrayList<>();
}
