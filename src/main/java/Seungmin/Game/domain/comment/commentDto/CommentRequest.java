package Seungmin.Game.domain.comment.commentDto;

import Seungmin.Game.domain.member.memberDto.Member;
import Seungmin.Game.domain.post.postDto.Post;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequest {

    private Long postId;
    private Long parentId;
    private String comment;

    public static Comment toEntity(CommentRequest commentRequest, Post post, Member member, Comment parent) {
        return Comment.builder()
                .post(post)
                .member(member)
                .parent(parent)
                .children(null)
                .comment(commentRequest.getComment()).build();
    }
}
