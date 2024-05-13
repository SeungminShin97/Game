package Seungmin.Game.domain.member.memberDto;

import Seungmin.Game.common.enums.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
public class MemberRequest {

    private String loginId;
    private String password;
    private String name;
    private String nickname;
    private Gender gender;
    private LocalDate birthday;

    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .name(name)
                .nickname(nickname)
                .gender(gender)
                .birthday(birthday)
                .build();
    }
}
