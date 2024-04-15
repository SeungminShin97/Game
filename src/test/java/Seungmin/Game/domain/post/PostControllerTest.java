package Seungmin.Game.domain.post;

import Seungmin.Game.domain.category.CategoryService;
import Seungmin.Game.domain.post.postDto.PostResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PostController.class)
@EnableSpringDataWebSupport
@WithMockUser
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PostService postService;
    @MockBean
    private CategoryService categoryService;

    @Test
    public void home() throws Exception {
        //given
        Page<PostResponse> mockPage = new PageImpl<>(Collections.singletonList(PostResponse.builder().build()), PageRequest.of(0,20), 1);
        when(postService.showPostList(any(Pageable.class))).thenReturn(mockPage);

        //when
        //then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("postList"))
                .andExpect(model().attribute("postList", hasSize(1)))
                .andExpect(view().name("post/list"));
        
        verify(postService).showPostList(any(Pageable.class));
    }

//    @Test
//    void changePostList() throws Exception {
//        //given
//        Page<PostResponse> mockPage = new PageImpl<>(Collections.singletonList(PostResponse.builder().build()), PageRequest.of(0,20), 1);
//        when(postService.findPostBySearchType(any(SearchDto.class))).thenReturn(mockPage);
//
//        //when
//        //then
//        mockMvc.perform(get("/post/list")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .param("searchType", "all")
//                        .param("categoryType", "all")
//                        .param("keyword",))
//                .andExpect(status().isOk())
//                .andExpect(mo)
//    }

    @Test
    void openPostWrite() {
    }

    @Test
    void savePost() {
    }

    @Test
    void viewPost() {
    }

    @Test
    void updatePost() {
    }
}