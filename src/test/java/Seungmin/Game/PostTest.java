package Seungmin.Game;

import Seungmin.Game.domain.category.Category;
import Seungmin.Game.domain.category.CategoryRepository;
import Seungmin.Game.domain.member.MemberRepository;
import Seungmin.Game.domain.member.memberDto.Member;
import Seungmin.Game.domain.post.PostRepository;
import Seungmin.Game.domain.post.postDto.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class PostTest {


    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MemberRepository memberRepository;


    @Test
    @Commit
    void asfd() {
        Category category = categoryRepository.findByCategory("test1");
        Member member = memberRepository.findByLoginId("test1").orElseThrow();
        postRepository.save(Post.builder().title("공개").member(member).category(category).content("test")
                .publicYn(true).noticeYn(false).build());
    }

    @Test
    @Commit
    void save() {
        // 컴퓨터가 힘들어해요~
        List<Category> categories = categoryRepository.findAll();
        System.out.println(categories.toString());
        List<Member> members = memberRepository.findAll();
        System.out.println(members.toString());

        List<Post> lists = categories.stream()
                .flatMap(category -> members.stream()
                        .flatMap(member -> IntStream.rangeClosed(0,100)
                                .mapToObj(i -> Post.builder()
                                        .title("test" + i)
                                        .member(member)
                                        .content("가나다라마바사")
                                        .publicYn(false)
                                        .category(category)
                                        .noticeYn(false)
                                        .build())))
                .toList();

        postRepository.saveAll(lists);
    }
}

