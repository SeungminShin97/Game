package Seungmin.Game.domain.comment;

import Seungmin.Game.domain.comment.commentDto.Comment;
import Seungmin.Game.domain.post.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentServiceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;


    @Test
    void saveComment() {

    }

    @Test
    void getComment() {
        List<Comment> list = commentRepository.findCommentsHierarchically(1124L);
        System.out.println(list);
//        Comment comment = commentRepository.findById(1L).orElse(null);
//        System.out.println(comment);
    }


    @Test
    void updateComment() {
    }

    @Test
    void deleteComment() {
    }
}