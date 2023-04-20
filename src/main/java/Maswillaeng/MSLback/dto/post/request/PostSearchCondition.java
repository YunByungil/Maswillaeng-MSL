package Maswillaeng.MSLback.dto.post.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchCondition {

    private String postWriter;
    private String title;
    private String PostContent;
    private String commentWriter;
    private String commentContent;
}
