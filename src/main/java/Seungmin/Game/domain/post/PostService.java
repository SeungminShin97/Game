package Seungmin.Game.domain.post;

import Seungmin.Game.domain.category.Category;
import Seungmin.Game.domain.category.CategoryRepository;
import Seungmin.Game.domain.post.postDto.Post;
import Seungmin.Game.domain.post.postDto.PostRequest;
import Seungmin.Game.domain.post.postDto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 리스트 반환
     * @return Category List
     */
    public List<Category> showCategoryList() {
        return categoryRepository.findAll();
    }

    /**
     * 게시글 리스트 반환
     * @return PostResponse List
     */
    public List<PostResponse> showPostList() { return postRepository.findByDeleteYn(false).stream().map(Post::toDto).toList(); }

    /**
     * 게시글 저장
     *
     * @param params 게시글 정보
     */
    @Transactional
    public void savePost(final PostRequest params) {
        Category category = categoryRepository.findByCategory(params.getCategory());
        Post post = params.toEntity(category);
        postRepository.save(post);
    }

    /**
     * 게시글 아이디로 카테고리를 포함한 게시글 검색
     * @param id 게시글 아이디
     * @return PostResponse
     */
    public PostResponse findPostByIdWithCategory(final Long id) {
        return postRepository.findByIdWithCategory(id).map(Post::toDto).orElse(null);
    }

    /**
     * 게시글 아이디로 게시글 검색
     * @param id 게시글 아이디
     * @return PostResponse
     */
    public PostResponse findPostById(final Long id) {
        Optional<Post> post = postRepository.findByIdAndDeleteYn(id, false);
        return post.map(Post::toDto) .orElse(null);
    }



    /**
     * 카테고리 이름으로 게시글 검색
     * @param categoryName 카테고리 이름
     * @return List<PostResponse>
     */
    public List<PostResponse> findPostByCategoryId(final String categoryName) {
        if(!categoryName.equals("전체글")) {
            Long id = categoryRepository.findByCategory(categoryName).getId();
            return postRepository.findByCategoryIdAndDeleteYn(id, false).stream().map(Post::toDto).collect(Collectors.toList());
        }
        else
            return postRepository.findAll().stream().map(Post::toDto).toList();
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public void updatePost(final PostRequest postRequest) {
        Post post = postRepository.findById(postRequest.getId()).orElse(null);
        Category category = categoryRepository.findByCategory(postRequest.getCategory());

        if(post != null && category != null)
            post.updatePost(postRequest, category);
    }

    /**
     * 게시글 조회수 증가
     */
    @Transactional
    public void updateViewCnt(final Long postId) {
        postRepository.findById(postId).ifPresent(Post::updateViewCnt);
    }
}