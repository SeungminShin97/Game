package Seungmin.Game.domain.post.postDto;

import Seungmin.Game.common.BaseTimeEntity;
import Seungmin.Game.domain.category.Category;
import jakarta.persistence.*;
import lombok.*;




@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private String writer;
    @Column
    private int viewCnt;
    @Column
    private boolean noticeYn;
    @Column
    private boolean deleteYn;
    @Column
    private boolean publicYn;


    @Builder
    public Post(Category category, String title, String content, String writer, int viewCnt, boolean noticeYn, boolean deleteYn,
                boolean publicYn) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.viewCnt = viewCnt;
        this.noticeYn = noticeYn;
        this.deleteYn = deleteYn;
        this.publicYn = publicYn;
    }

    public PostResponse toDto() {
        return PostResponse.builder()
                .category(category)
                .title(title)
                .content(content)
                .writer(writer)
                .viewCnt(viewCnt)
                .noticeYn(noticeYn)
                .deleteYn(deleteYn)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .publicYn(publicYn)
                .build();
    }
}
