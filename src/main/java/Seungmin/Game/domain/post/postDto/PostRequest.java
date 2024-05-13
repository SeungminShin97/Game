package Seungmin.Game.domain.post.postDto;

import Seungmin.Game.domain.category.Category;
import Seungmin.Game.domain.member.memberDto.Member;
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
    private Member member;
    private int viewCnt;
    private boolean publicYn;
    private boolean noticeYn;

    public Post toEntity(Category category) {
        return Post.builder()
                .category(category)
                .title(title)
                .content(content)
                .member(member)
                .viewCnt(viewCnt)
                .noticeYn(noticeYn)
                .publicYn(publicYn)
                .build();
    }


}
