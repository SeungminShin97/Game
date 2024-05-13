package Seungmin.Game.domain.member;

import Seungmin.Game.common.enums.Gender;
import Seungmin.Game.domain.member.memberDto.Member;
import Seungmin.Game.domain.post.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    private PostRepository postRepository;

    private PasswordEncoder passwordEncoder;
    private Member member;


    @BeforeEach
    public void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void saveMember() {
        //given
        Member member = Member.builder()
                .loginId("admin")
                .name("admin")
                .nickname("admin")
                .password(passwordEncoder.encode("1234"))
                .birthday(LocalDate.now())
                .gender(Gender.male)
                .build();

        //when
        Member testmember = memberRepository.save(member);

        //then
        Assertions.assertThat(testmember).isNotNull();
        Assertions.assertThat(testmember.getId()).isNotNull();
        Assertions.assertThat(testmember.getName()).isEqualTo("admin");
        Assertions.assertThat(passwordEncoder.matches("1234", testmember.getPassword())).isTrue();

    }

    @Test
    @Commit
    void saveMember100() {
        List<Member> list = new ArrayList<>(100);
        for(int i = 1; i <= 10; i++) {
            Member member = Member.builder()
                    .loginId("test" + i)
                    .name("test" + i)
                    .nickname("test" + i)
                    .password(passwordEncoder.encode("1234"))
                    .birthday(LocalDate.now())
                    .gender(Gender.male)
                    .build();
            list.add(member);
        }

        memberRepository.saveAll(list);
    }

    @Test
    void findAllMember() {
        System.out.println("=====================================================================");
        memberRepository.findAll().forEach(t -> System.out.println(t.toString()));
        System.out.println("=====================================================================");
    }
}