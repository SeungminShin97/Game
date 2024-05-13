package Seungmin.Game.domain.post.postDto;

import Seungmin.Game.domain.category.Category;
import Seungmin.Game.domain.member.memberDto.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostResponse {

    private Long id;
    private Category category;
    private String title;
    private String content;
    private Member member;
    private int viewCnt;
    private boolean noticeYn;
    private boolean publicYn;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


    @Builder
    public PostResponse(Long id, Category category, String title, String content, Member member,
                        int viewCnt, boolean noticeYn, LocalDateTime createdDate, LocalDateTime modifiedDate, boolean publicYn) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.content = content;
        this.member = member;
        this.viewCnt = viewCnt;
        this.noticeYn = noticeYn;
        this.publicYn = publicYn;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

}
