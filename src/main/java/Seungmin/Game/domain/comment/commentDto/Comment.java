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
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();
    @Column
    private boolean deleteYn = false;
    @Column
    private String comment;

    @Builder
    public Comment(Post post, Member member, Comment parent, List<Comment> children, boolean deleteYn, String comment, int likes) {
        this.post = post;
        this.member = member;
        this.parent = parent;
        this.children = children;
        this.deleteYn = deleteYn;
        this.comment = comment;
    }

    public static CommentResponse toDto(Comment comment) {
        return CommentResponse.builder()
                .id(comment.id)
                .postId(comment.post.getId())
                .nickname(comment.getMember().getNickname())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .children(new ArrayList<>())
                .deleteYn(comment.deleteYn)
                .comment(comment.getComment())
                .createdDate(comment.getCreatedDate())
                .modifiedDate(comment.getModifiedDate()).build();
    }

    public void setChildren(Comment children) {
        this.children.add(children);
        children.setParent(this);
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }

    public void deleteComment() {
        this.deleteYn = true;
    }

//      NPE 발생발생!
//    @Override
//    public String toString() {
//        String childrenIdList = children.stream().map(child -> child.id.toString()).collect(Collectors.joining(", "));
//        return "Comment{" +
//                "id=" + id +
//                ", postId=" + (post != null ? post.getId() : "null") +
//                ", memberId=" + (member != null ? member.getId() : "null") +
//                ", parentId=" + (parent != null ? parent.getId() : "null") +
//                ", childrenList=[" + childrenIdList + "]" +
//                ", deleteYn=" + deleteYn +
//                ", comment='" + comment + '\'' +
//                "}\n";
//    }
}
