package Seungmin.Game.domain.post;

import Seungmin.Game.domain.category.Category;
import Seungmin.Game.domain.post.postDto.Post;
import Seungmin.Game.domain.post.postDto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByDeleteYn(boolean deleteYn, Pageable pageable);
    Optional<Post> findByWriter(String writer);
    Page<Post> findByCategoryIdAndDeleteYn(Long categoryId, boolean deleteYn, Pageable pageable);
    Optional<Post> findByIdAndDeleteYn(Long categoryId, boolean deleteYn);

    @Query("SELECT p FROM Post p JOIN FETCH p.category WHERE p.id = :postId")
    Optional<Post> findByIdWithCategory(Long postId);

    void deleteByWriter(String writer);


}
