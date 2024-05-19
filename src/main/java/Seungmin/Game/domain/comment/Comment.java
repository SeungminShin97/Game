package Seungmin.Game.domain.comment;

import Seungmin.Game.common.BaseTimeEntity;
import Seungmin.Game.domain.member.memberDto.Member;
import Seungmin.Game.domain.post.postDto.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;
    @Column
    private boolean deleteYn;
    @Column
    private String comment;
    @Column
    private int likes;

    @Builder
    public Comment(Post post, Member member, boolean deleteYn, String comment, int likes) {
        this.post = post;
        this.member = member;
        this.deleteYn = deleteYn;
        this.comment = comment;
        this.likes = likes;
    }

    public void plusLike() { likes++; }
    public void minusLike() { likes--; }

}
