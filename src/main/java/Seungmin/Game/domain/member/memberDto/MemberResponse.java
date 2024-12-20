package Seungmin.Game.domain.member.memberDto;

import Seungmin.Game.common.enums.Gender;
import Seungmin.Game.common.enums.Provider;
import Seungmin.Game.common.enums.Role;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberResponse {

    private Long id;
    private String loginId;
    private String email;
    private String name;
    private String nickname;
    private Gender gender;
    private LocalDate birthday;
    private LocalDateTime createdDate;
    private Role role;
    private Provider provider;
    private String identifier;


}
