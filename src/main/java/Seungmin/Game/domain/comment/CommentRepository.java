package Seungmin.Game.domain.comment;

import Seungmin.Game.domain.comment.commentDto.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

//    @Query(value = "WITH RECURSIVE CommentCTE (id, post_id, member_id, parent_id, delete_yn, comment, created_date, modified_date, likes, level) AS( " +
//            "    SELECT id, post_id, member_id, parent_id, delete_yn, comment, created_date, modified_date, likes, 1 AS level " +
//            "    FROM Comment " +
//            "    WHERE post_id = :postId AND parent_id IS NULL " +
//            "    UNION ALL " +
//            "    SELECT c.id, c.post_id, c.member_id, c.parent_id, c.delete_yn, c.comment, c.created_date, c.modified_date, c.likes, cte.level + 1 " +
//            "    FROM Comment c " +
//            "    INNER JOIN CommentCTE cte ON c.parent_id = cte.id " +
//            ") " +
//            "SELECT id, post_id, member_id, parent_id, delete_yn, comment, created_date, modified_date, likes, level " +
//            "FROM CommentCTE " +
//            "ORDER BY level, parent_id, id", nativeQuery = true)
//    List<Comment> findComment(@Param("postId")Long postId);

    @Query("SELECT c FROM Comment c " +
            "JOIN FETCH c.post " +
            "LEFT JOIN FETCH c.parent " +
            "WHERE c.post.id = :postId " +
            "ORDER BY c.parent.id ASC NULLS FIRST, c.createdDate DESC, c.id ASC")
    List<Comment> findCommentsHierarchically(@Param("postId") Long postId);

}
