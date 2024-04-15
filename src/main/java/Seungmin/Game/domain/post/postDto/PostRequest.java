package Seungmin.Game.domain.post.postDto;

import Seungmin.Game.domain.category.Category;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequest {

    private Long id;
    private String category;
    private String title;
    private String content;
    private String writer;
    private int viewCnt;
    private boolean publicYn;
    private boolean noticeYn;

    public Post toEntity(Category category) {
        return Post.builder()
                .category(category)
                .title(title)
                .content(content)
                .writer(writer)
                .viewCnt(viewCnt)
                .noticeYn(noticeYn)
                .publicYn(publicYn)
                .build();
    }


}
