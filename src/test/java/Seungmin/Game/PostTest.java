package Seungmin.Game;

import Seungmin.Game.domain.category.Category;
import Seungmin.Game.domain.category.CategoryRepository;
import Seungmin.Game.domain.post.PostRepository;
import Seungmin.Game.domain.post.postDto.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PostTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    @Commit
    void save() {
        Category category = Category.builder().category("testtest").build();
        categoryRepository.save(category);
        List<Post> postList = new ArrayList<>();
        for(int i = 0; i < 1000; i++) {
            postList.add(Post.builder()
                    .title("test" + (i + 1000))
                    .writer("testWriter")
                    .content("가나다라마바사")
                    .publicYn(false)
                    .category(category)
                    .noticeYn(false)
                    .build());
        }

        postRepository.saveAll(postList);
    }
}
