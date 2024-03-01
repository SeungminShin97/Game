package Seungmin.Game.domain.post;

import Seungmin.Game.domain.category.Category;
import Seungmin.Game.domain.category.CategoryRepository;
import Seungmin.Game.domain.post.postDto.Post;
import Seungmin.Game.domain.post.postDto.PostRequest;
import Seungmin.Game.domain.post.postDto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 게시글 리스트 반환
     * @return Category List
     */
    public List<Category> showCategoryList() {
        return categoryRepository.findAll();
    }

    /**
     * 게시글 저장
     *
     * @param params - 게시글 정보
     */
    @Transactional
    public void savePost(final PostRequest params) {
        Category category = categoryRepository.findByCategory(params.getCategory());
        Post post = params.toEntity(category);
        postRepository.save(post);
    }

    /**
     * 게시글 검색
     *
     * @return Post
     */
    public PostResponse findPostById(final Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(Post::toDto).orElse(null);
    }
}
