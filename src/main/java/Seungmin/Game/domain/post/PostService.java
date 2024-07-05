package Seungmin.Game.domain.post;

import Seungmin.Game.common.dto.SearchDto;
import Seungmin.Game.common.exceptions.CustomException;
import Seungmin.Game.common.exceptions.CustomExceptionCode;
import Seungmin.Game.domain.category.Category;
import Seungmin.Game.domain.category.CategoryService;
import Seungmin.Game.domain.file.FileService;
import Seungmin.Game.domain.member.MemberRepository;
import Seungmin.Game.domain.member.MemberService;
import Seungmin.Game.domain.member.memberDto.Member;
import Seungmin.Game.domain.post.postDto.Post;
import Seungmin.Game.domain.post.postDto.PostRequest;
import Seungmin.Game.domain.post.postDto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final FileService fileService;


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
    public void savePost(final PostRequest params, final Authentication authentication, final ArrayList<MultipartFile> files) {
        try {
            Category category = categoryService.showCategoryByCategory(params.getCategory());
            Member member = memberService.getMemberByAuthentication(authentication);
            params.setMember(member);
            Post post = params.toEntity(category);
            postRepository.save(post);

            try {
                fileService.savePostFile(post, files);
            } catch (Exception e) {
                hardDeletePost(post.getId());
                throw new CustomException(CustomExceptionCode.FileCreateFailedException, e.getMessage());
            }
        } catch (CustomException e) {   // 사용자 검색 실패
            throw e;
        } catch (Exception e) {
            throw new CustomException(CustomExceptionCode.PostSaveFailedException);
        }
    }

    /**
     * 게시글 아이디로 게시글 검색
     * @param id 게시글 아이디
     * @return PostResponse
     */
    public Post findPostById(final Long id) {
        return postRepository.findByIdAndDeleteYn(id, false).orElseThrow(() -> new CustomException(CustomExceptionCode.PostNotFoundException));
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
    public void updatePost(final PostRequest postRequest, final Authentication authentication, final ArrayList<MultipartFile> files) {
        try {
            postRequest.setMember(memberService.getMemberByAuthentication(authentication));
            Post post = postRepository.findById(postRequest.getId()).orElse(null);
            Category category = categoryService.showCategoryByCategory(postRequest.getCategory());

            if(post != null && category != null)
                post.updatePost(postRequest, category);

            try {
                fileService.updatePostFile(post, files);
            } catch (Exception e) {
                throw new CustomException(CustomExceptionCode.FileUpdateFailedException, e.getMessage());
            }
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(CustomExceptionCode.PostUpdateFailedException);
        }
    }


    /**
     * 게시글 물리 삭제
     * @param postId
     */
    @Transactional
    public void hardDeletePost(final Long postId) {
        try {
            postRepository.deleteById(postId);
        } catch (Exception e) {
            throw new CustomException(CustomExceptionCode.PostDeleteFailedException, "게시글 물리 삭제 실패");
        }
    }

    /**
     * 게시글 논리 삭제
     * @param postId
     */
    @Transactional
    public void softDeletePost(final long postId) {
        try {
            Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(CustomExceptionCode.PostNotFoundException));
            post.deletePost();
        } catch (Exception e) {
            throw new CustomException(CustomExceptionCode.PostDeleteFailedException, "게시글 논리 삭제 실패");
        }
    }

    /**
     * 게시글 조회수 증가
     */
    @Transactional
    public void updateViewCnt(final Long postId) {
        postRepository.findById(postId).ifPresent(Post::updateViewCnt);
    }


    /**
     * 로그인 유저와 게시글 작성자 비교
     * @param authentication
     * @param postId
     * @return
     */
    public boolean compareLoginIdAndPostWriter(Authentication authentication, long postId) {
        try {
            final String loginId = memberService.getMemberByAuthentication(authentication).getLoginId();
            final long memberId = memberRepository.findByLoginId(loginId).orElseThrow(() -> new UsernameNotFoundException("유저 검색 실패")).getId();

            final long postWriterId = findPostById(postId).getMember().getId();
            return memberId == postWriterId;
        } catch (CustomException e) {
            throw e;
        }
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