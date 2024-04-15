package Seungmin.Game.domain.post;

import Seungmin.Game.common.dto.SearchDto;
import Seungmin.Game.domain.category.Category;
import Seungmin.Game.domain.category.CategoryService;
import Seungmin.Game.domain.post.postDto.Post;
import Seungmin.Game.domain.post.postDto.PostRequest;
import Seungmin.Game.domain.post.postDto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;


    /**
     * 게시글 리스트 반환
     * @return PostResponse List
     */
    public Page<PostResponse> showPostList(Pageable pageable) {
        return postRepository.findByDeleteYn(false, pageable).map(Post::toDto);
    }

    /**
     * 게시글 저장
     *
     * @param params 게시글 정보
     */
    @Transactional
    public void savePost(final PostRequest params) {
        Category category = categoryService.showCategoryByCategory(params.getCategory());
        Post post = params.toEntity(category);
        postRepository.save(post);
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
     * 게시글 검색 (검색 조건 포함)
     * @param searchDto 검색 조건 (searchType, categoryType, keyword, pageable)
     * @return List<PostResponse>
     */
    public Page<PostResponse> findPostBySearchType(final SearchDto searchDto, final Pageable pageable) {
        if(searchDto.getCategoryType().equals("전체글"))
            searchDto.setCategoryType(null);
        return switch (searchDto.getSearchType()) {
            case title -> searchByTitle(searchDto, pageable);
            case content -> searchByContent(searchDto, pageable);
            case writer -> searchByWriter(searchDto, pageable);
            case all -> searchAll(searchDto, pageable);
        };
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public void updatePost(final PostRequest postRequest) {
        Post post = postRepository.findById(postRequest.getId()).orElse(null);
        Category category = categoryService.showCategoryByCategory(postRequest.getCategory());

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

    private Page<PostResponse> searchByTitle(final SearchDto searchDto, final Pageable pageable) {
        return postRepository.searchTitle(searchDto.getCategoryType(), searchDto.getKeyword(), pageable).map(Post::toDto);
    }
    private Page<PostResponse> searchByContent(final SearchDto searchDto, final Pageable pageable) {
        return postRepository.searchContent(searchDto.getCategoryType(), searchDto.getKeyword(), pageable).map(Post::toDto);
    }
    private Page<PostResponse> searchByWriter(final SearchDto searchDto, final Pageable pageable) {
        return postRepository.searchWriter(searchDto.getCategoryType(), searchDto.getKeyword(), pageable).map(Post::toDto);
    }
    private Page<PostResponse> searchAll(final SearchDto searchDto, final Pageable pageable) {
        return postRepository.searchAll(searchDto.getCategoryType(), searchDto.getKeyword(), pageable).map(Post::toDto);
    }

}