package Seungmin.Game.domain.post;

import Seungmin.Game.common.dto.MessageDto;
import Seungmin.Game.common.dto.SearchDto;
import Seungmin.Game.domain.category.CategoryService;
import Seungmin.Game.domain.member.MemberRepository;
import Seungmin.Game.domain.post.postDto.PostRequest;
import Seungmin.Game.domain.post.postDto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CategoryService categoryService;
    private final MemberRepository memberRepository;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    // 메세지 전달 후 리다이렉트
    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }

    // asideBar에 카테고리 리스트 전달
    public void renderCategoryListForAsideBar(Model model) {
        model.addAttribute("categoryList", categoryService.showCategoryList());
    }

    // 홈 화면
    // 게시글 리스트 페이지
    @GetMapping("/")
    @PostMapping("/")
    public String home(@PageableDefault(sort = "id", size = 20) Pageable pageable, Principal principal, Model model) {
        renderCategoryListForAsideBar(model);

        if(principal != null)
            model.addAttribute("loginUser", principal.getName());
        return "post/list";
    }


    // 게시글 카테고리 변경
    @GetMapping("/post/list")
    @ResponseBody
    public Page<PostResponse> changePostList(@ModelAttribute final SearchDto searchDto, @PageableDefault(sort = "id", size = 20) Pageable pageable) {
        return postService.findPostBySearchType(searchDto, pageable);
    }


    // 게시글 작성 페이지
    @GetMapping("/post/write")
    public String openPostWrite(@RequestParam(value = "id", required = false) final Long id, Model model, Principal principal) {
        if(id != null) {    // 게시글 수정
            try{
                if(!postService.compareLoginIdAndPostWriter(principal, id)) // 로그인 유저와 게시글 작성자 비교
                    return showMessageAndRedirect(new MessageDto("게시글 수정은 작성자만 가능합니다.", "/post/view/" + id, RequestMethod.GET, null), model);
                renderCategoryListForAsideBar(model);
                PostResponse post = postService.findPostById(id);
                model.addAttribute("post", post);
            } catch (Exception e) {
                MessageDto messageDto = MessageDto.builder()
                        .message(e.getMessage())
                        .redirectUri("/")
                        .method(RequestMethod.GET)
                        .data(null).build();
                return showMessageAndRedirect(messageDto, model);
            }
        }
        return "post/write";
    }

    // 게시글 생성
    @PostMapping("/post/save")
    public String savePost(@ModelAttribute final PostRequest params, Model model) {
        postService.savePost(params);
        MessageDto messageDto = new MessageDto("게시글 생성이 완료되었습니다.", "/", RequestMethod.GET, null);
        return showMessageAndRedirect(messageDto, model);
    }

    // 게시글 상세 페이지
    @GetMapping("/post/view/{postId}")
    public String viewPost(@PathVariable Long postId, Model model, Principal principal) {
        try {
            final PostResponse postResponse = postService.findPostById(postId);
            if(!postResponse.isPublicYn() && principal == null)
                return showMessageAndRedirect(new MessageDto("비공개 게시글입니다. 로그인 후 이용해 주세요", "/", RequestMethod.GET, null), model);
            renderCategoryListForAsideBar(model);
            postService.updateViewCnt(postId);
            PostResponse post = postService.findPostById(postId);
            model.addAttribute("post", post);
            return "post/view";
        } catch (Exception e) {
            MessageDto messageDto = new MessageDto(e.getMessage(), "/", RequestMethod.GET, null);
            return showMessageAndRedirect(messageDto, model);
        }
    }

    // 게시글 수정 페이지
    @PutMapping("/post/update")
    public String updatePost(@ModelAttribute final PostRequest params, Model model) {
        postService.updatePost(params);
        return showMessageAndRedirect(new MessageDto("게시글 수정이 완료되었습니다.", "/", RequestMethod.GET, null), model);
    }

}




