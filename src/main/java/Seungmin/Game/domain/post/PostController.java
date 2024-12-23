package Seungmin.Game.domain.post;

import Seungmin.Game.common.dto.ApiResponseDto;
import Seungmin.Game.common.dto.NotificationDto;
import Seungmin.Game.common.dto.SearchDto;
import Seungmin.Game.common.exceptions.CustomException;
import Seungmin.Game.domain.category.CategoryService;
import Seungmin.Game.domain.file.FileService;
import Seungmin.Game.domain.file.fileDto.FileResponse;
import Seungmin.Game.domain.member.MemberService;
import Seungmin.Game.domain.post.postDto.PostRequest;
import Seungmin.Game.domain.post.postDto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;
    private final CategoryService categoryService;
    private final MemberService memberService;
    private final FileService fileService;


    // 메세지 전달 후 리다이렉트
    private String showMessageAndRedirect(final NotificationDto params, Model model) {
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
    public String home(@PageableDefault(sort = "id", size = 20) Pageable pageable, Model model) {
        renderCategoryListForAsideBar(model);
        return "post/list";
    }


    // 게시글 카테고리 변경
    @GetMapping("/post/list")
    @ResponseBody
    public Page<PostResponse> changePostList(@ModelAttribute final SearchDto searchDto, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 20) Pageable pageable) {
        return postService.findPostBySearchType(searchDto, pageable);
    }


    // 게시글 작성 페이지
    @GetMapping("/post/write")
    public String openPostWrite(@RequestParam(value = "id", required = false) final Long id, Model model, Authentication authentication) {
        renderCategoryListForAsideBar(model);
        try{
            String nickname = memberService.getMemberByAuthentication(authentication).getNickname();
            model.addAttribute("writer", nickname);
        } catch (Exception e) {
            NotificationDto notificationDto = new NotificationDto(e.getMessage(), "/", RequestMethod.GET, null);
            return showMessageAndRedirect(notificationDto, model);
        }

        if(id != null) {    // 게시글 수정
            try{
                if(!postService.compareLoginIdAndPostWriter(authentication, id)) // 로그인 유저와 게시글 작성자 비교
                    return showMessageAndRedirect(new NotificationDto("게시글 수정은 작성자만 가능합니다.", "/post/view/" + id, RequestMethod.GET, null), model);
                PostResponse post = postService.findPostById(id).toDto();
                model.addAttribute("post", post);
                List<FileResponse> existFileList = fileService.getPostFile(id);
                model.addAttribute("existFileList", existFileList);
            } catch (Exception e) {
                NotificationDto notificationDto = NotificationDto.builder()
                        .message(e.getMessage())
                        .redirectUri("/")
                        .method(RequestMethod.GET)
                        .data(null).build();
                return showMessageAndRedirect(notificationDto, model);
            }
        }
        return "post/write";
    }

    // 게시글 생성
    @PostMapping("/post/save")
    @ResponseBody
    public ApiResponseDto savePost(@ModelAttribute final PostRequest params, @RequestParam final ArrayList<MultipartFile> files) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            postService.savePost(params, authentication, files);
            return ApiResponseDto.builder().successStatus(true).postSaved(true).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResponseDto.builder().successStatus(false).errorMessage(e.getMessage()).build();
        }
    }

    // 게시글 상세 페이지
    @GetMapping("/post/view/{postId}")
    public String viewPost(@PathVariable Long postId, Model model, Principal principal) {
        try {
            final PostResponse postResponse = postService.findPostById(postId).toDto();
            if(!postResponse.isPublicYn() && principal == null)
                return showMessageAndRedirect(new NotificationDto("비공개 게시글입니다. 로그인 후 이용해 주세요", "/", RequestMethod.GET, null), model);

            renderCategoryListForAsideBar(model);

            postService.updateViewCnt(postId);

            PostResponse post = postService.findPostById(postId).toDto();
            List<FileResponse> files = fileService.getPostFile(postId);

            model.addAttribute("post", post);
            model.addAttribute("files", files);

            return "post/view";
        } catch (Exception e) {
            NotificationDto notificationDto = new NotificationDto(e.getMessage(), "/", RequestMethod.GET, null);
            return showMessageAndRedirect(notificationDto, model);
        }
    }

    // 게시글 수정 페이지
    @PutMapping("/post/update")
    @ResponseBody
    public ApiResponseDto updatePost(@ModelAttribute final PostRequest params, @RequestParam final ArrayList<MultipartFile> files) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            postService.updatePost(params, authentication, files);
            return ApiResponseDto.builder().successStatus(true).postSaved(false).build();
        } catch (CustomException e) {
            logger.error(e.getMessage());
            return ApiResponseDto.builder().successStatus(false).errorMessage("알수 없는 오류가 발생했습니다. 다시 시도해 주세요").build();
        } catch (Exception e) {
            return ApiResponseDto.builder().successStatus(false).errorMessage(e.getMessage()).build();
        }
    }

    // 게시글 삭제
    @GetMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable Long postId, Model model) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(postService.compareLoginIdAndPostWriter(authentication, postId)) {
                postService.softDeletePost(postId);
                return showMessageAndRedirect(new NotificationDto("게시글 삭제가 완료되었습니다.", "/", RequestMethod.GET, null), model);
            } else {
                return showMessageAndRedirect(new NotificationDto("게시글 작성자만 삭제할 수 있습니다.", "/post/view/" + postId, RequestMethod.GET, null), model);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return showMessageAndRedirect(new NotificationDto("알수 없는 오류가 발생했습니다. 다시 시도해 주세요", "/", RequestMethod.GET, null), model);
        }
    }

}




