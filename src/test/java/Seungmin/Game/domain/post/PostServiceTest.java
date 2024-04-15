package Seungmin.Game.domain.post;

import Seungmin.Game.domain.category.Category;
import Seungmin.Game.domain.category.CategoryRepository;
import Seungmin.Game.domain.post.postDto.Post;
import Seungmin.Game.domain.post.postDto.PostRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;
    private Post post;

    @BeforeEach
    void setUp() {
        category = Category.builder().category("testtest").build();
        post = Post.builder().title("testTitle").writer("testWriter").content("가나다라마바사").publicYn(false).category(category).noticeYn(false).build();
    }

    @Test
    void showPostList() {//given
        categoryRepository.findAll().forEach(System.out::println);
    }

    @Test
    void savePost() {
        //given
        //when
        Post testPost = postRepository.save(post);

        //then
        Assertions.assertThat(testPost).isNotNull();
        Assertions.assertThat(testPost.getId()).isNotNull();
        Assertions.assertThat(testPost.getTitle()).isEqualTo("testTitle");
        Assertions.assertThat(testPost.getWriter()).isEqualTo("testWriter");
        Assertions.assertThat(testPost.getContent()).isEqualTo("가나다라마바사");
        Assertions.assertThat(testPost.isPublicYn()).isEqualTo(false);
        Assertions.assertThat(testPost.isNoticeYn()).isEqualTo(false);
        Assertions.assertThat(testPost.getCategory()).isEqualTo(category);
        Assertions.assertThat(testPost.getViewCnt()).isEqualTo(0);

    }

    @Test
    void findPostById() {
        //given
        postRepository.save(post);
        final long id = post.getId();

        //when
        Post testPost = postRepository.findById(id).orElse(null);

        //then
        Assertions.assertThat(testPost).isNotNull();
        Assertions.assertThat(testPost).isEqualTo(post);
        Assertions.assertThat(testPost.getCategory()).isEqualTo(category);
    }

    @Test
    void findPostBySearchType() {
        //given
        PageRequest pageable = PageRequest.of(0, 10);

        //when
        postRepository.save(post);
        categoryRepository.save(category);
        List<Page<Post>> list = List.of(
            postRepository.searchAll(category.getCategory(), "가나다", pageable),
            postRepository.searchAll(category.getCategory(), "가나다", pageable),
            postRepository.searchAll(category.getCategory(), "마바사", pageable),
            postRepository.searchAll(category.getCategory(), "Writer", pageable),
            postRepository.searchAll(category.getCategory(), "Title", pageable),
            postRepository.searchTitle(category.getCategory(), "Title", pageable),
            postRepository.searchContent(category.getCategory(), "가나다", pageable),
            postRepository.searchContent(category.getCategory(), "마바사", pageable),
            postRepository.searchWriter(category.getCategory(), "Writer", pageable)
        );

        //then
        list.forEach(t -> {
            Assertions.assertThat(t.getContent()).hasSize(1);
            Assertions.assertThat(t.getContent().get(0)).isEqualTo(post);
        });

    }

    @Test
    void asef() {
        PageRequest pageable = PageRequest.of(0, 10);
        System.out.println(postRepository.searchAll(category.getCategory(), "가나다", pageable).toString());
    }

    @Test
    void updatePost() {
        //given
        String updateContent = "updated content";
        PostRequest postRequest = PostRequest.builder().id(post.getId()).category(category.getCategory()).viewCnt(0).title(post.getTitle()).content(updateContent)
                .noticeYn(post.isNoticeYn()).publicYn(post.isPublicYn()).writer(post.getWriter()).build();

        //when
        post.updatePost(postRequest, category);
        Post updatedPost = postRepository.save(post);

        //then
        Assertions.assertThat(updatedPost.getContent()).isEqualTo(updateContent);

    }

    @Test
    void updateViewCnt() {
        //given
        int count = post.getViewCnt();

        //when
        post.updateViewCnt();
        postRepository.save(post);

        //then
        Assertions.assertThat(post.getViewCnt()).isEqualTo(count + 1);

    }

    @Test
    void findByIdGivenNonexistentId() {
        //given
        final long nonexistentId = 999L;

        //when
        Optional<Post> nonPost = postRepository.findById(nonexistentId);

        //then
        Assertions.assertThat(nonPost).isNotPresent();
    }
}