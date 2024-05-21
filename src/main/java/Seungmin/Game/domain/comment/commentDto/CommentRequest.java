package Seungmin.Game.domain.comment.commentDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequest {

    private Long id;
    private Long postId;
    private String nickname;
    private Long parentId;
    private List<Comment>
}
