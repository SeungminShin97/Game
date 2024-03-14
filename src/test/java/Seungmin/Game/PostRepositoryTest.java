package Seungmin.Game;

import Seungmin.Game.domain.category.Category;
import Seungmin.Game.domain.category.CategoryRepository;
import Seungmin.Game.domain.post.PostService;
import Seungmin.Game.domain.post.postDto.Post;
import Seungmin.Game.domain.post.PostRepository;
import Seungmin.Game.domain.post.postDto.PostResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@SpringBootTest
//@Transactional
public class PostRepositoryTest {


    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PostService postService;

    @Test
    public void postSaveTest() {
        List<Category> category = categoryRepository.findAll();
        List<Post> post = new ArrayList<>();

        category.forEach(t -> {
            int count = 10;
            while(count --> 0) {
                Post p = Post.builder()
                        .category(t)
                        .title("test" + t.getCategory() + count)
                        .content("본문")
                        .writer("admin")
                        .publicYn(true)
                        .build();
                post.add(p);
            }
        });

        postRepository.saveAll(post);
    }

//    @Test
//    public void postFindByWriterTest() {
//        List<Post> list = postRepository.findByWriter("admin");
//        list.forEach(System.out::println);
//    }

    @Test
    public void DeleteAllTest() {
        postRepository.deleteAll();
    }

    @Test
    public void postFindAll() {
        List<Post> postList = postRepository.findAll().stream().toList();
        postList.forEach(System.out::println);
    }

//    @Test
//    public void showPostListTest() {
//        Long id = categoryRepository.findByCategory("test1").getId();
//        List<PostResponse> post = postRepository.findByCategoryId(id).stream().filter(t -> !t.isDeleteYn()).map(Post::toDto).toList();
//        post.forEach(System.out::println);
//    }

    @Test
    @Transactional
    public void updatePostTest() {
        Post post = postRepository.findById(123L).orElse(null);
        System.out.println(post);

        Category category = categoryRepository.findByCategory("test1");
        PostResponse postResponse = PostResponse.builder().id(123L).category(category)
                .title("qwer").content("qwerqwer").writer("qwerqwerqwer")
                .viewCnt(100).noticeYn(true).publicYn(true).build();

//        postService.updatePost(postResponse);

        System.out.println(postRepository.findById(123L));

    }

}
