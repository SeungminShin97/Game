package Seungmin.Game.domain.comment;

import Seungmin.Game.domain.comment.commentDto.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

//    @Transactional
//    public void saveComment(Long postId, Long memberId, Long parentId,) {};

    public Page<Comment> findComment() {
        return null;
    }

    @Transactional
    public void updateComment() {};

    @Transactional
    public void deleteComment() {};

}
