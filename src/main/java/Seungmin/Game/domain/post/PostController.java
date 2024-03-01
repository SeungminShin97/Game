package Seungmin.Game.domain.post;

import Seungmin.Game.common.dto.MessageDto;
import Seungmin.Game.domain.category.Category;
import Seungmin.Game.domain.post.postDto.Post;
import Seungmin.Game.domain.post.postDto.PostRequest;
import Seungmin.Game.domain.post.postDto.PostResponse;
import lombok.RequiredArgsConstructor;
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
    @GetMapping(value = {"/", "/post/list"})
    public String home(Model model) {
        List<Category> list = postService.showCategoryList();
        model.addAttribute("categoryList", list);
        return "post/list";
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
                MessageDto messageDto = new MessageDto("존재하지 않는 게시글입니다.", "/post/list", RequestMethod.GET, null);
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
        MessageDto messageDto = new MessageDto("게시글 생성이 완료되었습니다.", "/post/list", RequestMethod.GET, null);
        return showMessageAndRedirect(messageDto, model);
    }



}
