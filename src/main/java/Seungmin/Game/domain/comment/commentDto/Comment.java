package Seungmin.Game.domain.comment.commentDto;

import Seungmin.Game.common.BaseTimeEntity;
import Seungmin.Game.domain.member.memberDto.Member;
import Seungmin.Game.domain.post.postDto.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();
    @Column
    private boolean deleteYn = false;
    @Column
    private String comment;
    @Column
    private int likes = 0;

    @Builder
    public Comment(Post post, Member member, Comment parent, List<Comment> children, boolean deleteYn, String comment, int likes) {
        this.post = post;
        this.member = member;
        this.parent = parent;
        this.children = children;
        this.deleteYn = deleteYn;
        this.comment = comment;
        this.likes = likes;
    }

    public void plusLike() { likes++; }
    public void minusLike() { likes--; }

}
