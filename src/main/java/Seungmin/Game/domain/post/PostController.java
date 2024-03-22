package Seungmin.Game.domain.post;

import Seungmin.Game.common.dto.MessageDto;
import Seungmin.Game.domain.category.Category;
import Seungmin.Game.domain.post.postDto.Post;
import Seungmin.Game.domain.post.postDto.PostRequest;
import Seungmin.Game.domain.post.postDto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    // 메세지 전달 후 리다이렉트
    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }

    // 홈 화면
    // 게시글 리스트 페이지
    @GetMapping("/")
    public String home(@PageableDefault(size = 20, sort = "id")Pageable pageable, Model model) {
        List<Category> categoryList = postService.showCategoryList();
        model.addAttribute("categoryList", categoryList);

//        List<PostResponse> postList = postService.showPostList();
        Page<PostResponse> postList = postService.showPostList(pageable);
        model.addAttribute("postList", postList);
        return "post/list";
    }

    // 게시글 카테고리 변경
    @GetMapping("/post/list/{category}")
    @ResponseBody
    public Page<PostResponse> changePostListByCategory(@PathVariable String category, @PageableDefault(size = 20, sort = "id")Pageable pageable) {
         return postService.findPostByCategoryId(category, pageable);
    }


    // 게시글 작성 페이지
    @GetMapping("/post/write")
    public String openPostWrite(@RequestParam(value = "id", required = false) final Long id, Model model) {
        if(id != null) {
            PostResponse post = postService.findPostById(id);
            if(post != null) {
                model.addAttribute("post", post);
            }
            else {
                MessageDto messageDto = new MessageDto("존재하지 않는 게시글입니다.", "/", RequestMethod.GET, null);
                return showMessageAndRedirect(messageDto, model);
            }
        }
        List<Category> list = postService.showCategoryList();
        model.addAttribute("categoryList", list);
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
    public String viewPost(@PathVariable Long postId, Model model) {
        // 오른쪽 asdie 카테고리 리스트
        List<Category> categoryList = postService.showCategoryList();
        model.addAttribute("categoryList", categoryList);

        if(postService.findPostById(postId) != null) {
            postService.updateViewCnt(postId);
            PostResponse post = postService.findPostByIdWithCategory(postId);
            model.addAttribute("post", post);
            return "post/view";
        }
        else
            return showMessageAndRedirect(new MessageDto("존재하지 않는 게시글입니다.", "/", RequestMethod.GET, null), model);
    }

    // 게시글 수정 페이지
    @PutMapping("/post/update")
    public String updatePost(@ModelAttribute final PostRequest params, Model model) {
        postService.updatePost(params);
        return showMessageAndRedirect(new MessageDto("게시글 수정이 완료되었습니다.", "/", RequestMethod.GET, null), model);
    }

}




//content: 현재 페이지에 포함된 데이터를 나타냅니다. 주로 리스트 형태로 반환됩니다.
//number: 현재 페이지 번호를 나타냅니다.
//size: 페이지당 표시되는 데이터의 수를 나타냅니다.
//        totalElements: 전체 데이터의 수를 나타냅니다.
//totalPages: 전체 페이지 수를 나타냅니다.
//first: 현재 페이지가 첫 번째 페이지인지 여부를 나타냅니다.
//        last: 현재 페이지가 마지막 페이지인지 여부를 나타냅니다.
//numberOfElements: 현재 페이지에 포함된 요소의 수를 나타냅니다. 보통 content의 길이와 동일합니다.
//sort: 정렬 정보를 나타냅니다. 주로 사용되는 경우는 없지만, 필요한 경우 사용할 수 있습니다.
//empty: 현재 페이지가 비어있는지 여부를 나타냅니다.