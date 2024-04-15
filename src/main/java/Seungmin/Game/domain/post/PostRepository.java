package Seungmin.Game.domain.post;

import Seungmin.Game.domain.post.postDto.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByDeleteYn(boolean deleteYn, Pageable pageable);
    Optional<Post> findByIdAndDeleteYn(Long categoryId, boolean deleteYn);

    @Query("SELECT p FROM Post p JOIN FETCH p.category WHERE " +
            "p.deleteYn = false " +
            "AND (:categoryType IS NULL OR p.category.category = :categoryType) " +
            "AND (:keyword IS NULL" +
            "   OR p.title LIKE CONCAT('%', :keyword, '%') " +
            "   OR p.content LIKE CONCAT('%', :keyword, '%') " +
            "   OR p.writer LIKE CONCAT('%', :keyword, '%'))")
    Page<Post> searchAll(@Param("categoryType")String categoryType,
                                  @Param("keyword")String keyword,
                                  Pageable pageable);
    @Query("SELECT p FROM Post p JOIN FETCH p.category WHERE " +
            "p.deleteYn = false " +
            "AND (:categoryType IS NULL OR p.category.category = :categoryType) " +
            "AND (:keyword IS NULL OR p.content LIKE CONCAT('%', :keyword, '%'))")
    Page<Post> searchContent(@Param("categoryType")String categoryType,
                             @Param("keyword")String keyword,
                             Pageable pageable);

    @Query("SELECT p FROM Post p JOIN FETCH p.category WHERE " +
            "p.deleteYn = false " +
            "AND (:categoryType IS NULL OR p.category.category = :categoryType) " +
            "AND (:keyword IS NULL OR p.title LIKE CONCAT('%', :keyword, '%'))")
    Page<Post> searchTitle(@Param("categoryType")String categoryType,
                           @Param("keyword")String keyword,
                           Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN p.category WHERE " +
            "p.deleteYn = false " +
            "AND (:categoryType IS NULL OR p.category.category = :categoryType) " +
            "AND (:keyword IS NULL OR p.writer LIKE CONCAT('%', :keyword, '%'))")
    Page<Post> searchWriter(@Param("categoryType")String categoryType,
                           @Param("keyword")String keyword,
                           Pageable pageable);

}
