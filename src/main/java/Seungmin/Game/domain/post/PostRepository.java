package Seungmin.Game.domain.post;

import Seungmin.Game.domain.post.postDto.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByWriter(String writer);

    void deleteByWriter(String writer);
}
