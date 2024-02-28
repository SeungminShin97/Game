package Seungmin.Game.domain.post;

import Seungmin.Game.common.dto.MessageDto;
import Seungmin.Game.domain.category.Category;
import Seungmin.Game.domain.post.postDto.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    // 메세지 전달 후 리다이렉트
    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }

    //
    @GetMapping("/")
    public String home(Model model) {
        List<Category> list = postService.showCategoryList();
        model.addAttribute("categoryList", list);
        return "post/list";
    }

    // 게시글 작성 페이지
    @GetMapping("/post/write.do")
    public String openPostWrite(@RequestParam(value = "id", required = false) final Long id, Model model) {
        return "post/write";
    }

    // 게시글 생성
    @PostMapping("/post/save.do")
    public String savePost(@ModelAttribute final PostRequest params, Model model) {
        postService.savePost(params);
        MessageDto messageDto = new MessageDto("게시글 생성이 완료되었습니다.", "/post/list.do", RequestMethod.GET, null);
        return showMessageAndRedirect(messageDto, model);
    }

    @GetMapping("post/list.do")
    public String openPostList(Model model) {
        return "post/list";
    }


}
