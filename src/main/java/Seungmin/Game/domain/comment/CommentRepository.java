package Seungmin.Game.domain.comment;

import Seungmin.Game.domain.comment.commentDto.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
