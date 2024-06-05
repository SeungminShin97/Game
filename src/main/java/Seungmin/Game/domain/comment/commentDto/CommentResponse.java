package Seungmin.Game.domain.comment.commentDto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentResponse {
    private Long id;
    private Long postId;
    private String nickname;
    private Long parentId;
    private List<CommentResponse> children;
    private boolean deleteYn;
    private String comment;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public void setChildren(CommentResponse commentResponse) {
        this.children.add(commentResponse);
    }

    public void checkDeleteYn() {
        if(deleteYn) {
            comment = "";
            nickname = "";
        }
    }


}
