package Seungmin.Game.domain.member.memberDto;

import Seungmin.Game.common.enums.Gender;
import Seungmin.Game.common.enums.Provider;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberRequest {

    private String loginId;
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 아닙니다.")
    private String email;
    private String password;
    private String name;
    private String nickname;
    private Gender gender;
    private LocalDate birthday;
    private Provider provider;

    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .loginId(loginId)
                .email(email)
                .password(encodedPassword)
                .name(name)
                .nickname(nickname)
                .gender(gender)
                .birthday(birthday)
                .provider(provider)
                .build();
    }
}
