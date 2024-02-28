package Seungmin.Game.domain.post;

import Seungmin.Game.domain.category.Category;
import Seungmin.Game.domain.category.CategoryRepository;
import Seungmin.Game.domain.post.postDto.Post;
import Seungmin.Game.domain.post.postDto.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * @param params - 게시글 정보
     * @return Generated PK
     */
    @Transactional
    public Long savePost(final PostRequest params) {
        Post post = params.toEntity();
        postRepository.save(post);
        return post.getId();
    }
}
