package Seungmin.Game.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

}
