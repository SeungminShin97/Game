package Seungmin.Game;

import Seungmin.Game.domain.category.Category;
import Seungmin.Game.domain.category.CategoryRepository;
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
import java.util.List;

@Slf4j
@SpringBootTest
//@Transactional
public class PostRepositoryTest {


    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void postSaveTest() {
        Category category = categoryRepository.findByCategory("test");
        for(int i = 0; i < 10; i++) {
            postRepository.save(Post.builder()
                    .category(category)
                    .title(Integer.toString(i))
                    .content("본문")
                    .writer("admin")
                    .publicYn(true)
                    .build());
        }
        Category category2 = categoryRepository.findByCategory("test1");
        for(int i = 0; i < 10; i++) {
            postRepository.save(Post.builder()
                    .category(category)
                    .title(Integer.toString(i))
                    .content("본문")
                    .writer("admin")
                    .publicYn(true)
                    .build());
        }

    }

    @Test
    public void postFindByWriterTest() {
        List<Post> list = postRepository.findByWriter("admin");
        list.forEach(System.out::println);
    }

    @Test
    public void DeleteAllTest() {
        postRepository.deleteAll();
    }

    @Test
    public void postFindAll() {
        List<Post> postList = postRepository.findAll().stream().toList();
        postList.forEach(System.out::println);
    }





}
