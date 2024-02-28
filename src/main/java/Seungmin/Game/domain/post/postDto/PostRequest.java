package Seungmin.Game.domain.post.postDto;

import Seungmin.Game.domain.category.Category;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequest {

    private Category category;
    private String title;
    private String content;
    private String writer;
    private boolean publicYn;

    public Post toEntity() {
        return Post.builder()
                .category(category)
                .title(title)
                .content(content)
                .writer(writer)
                .publicYn(publicYn)
                .build();
    }


}
