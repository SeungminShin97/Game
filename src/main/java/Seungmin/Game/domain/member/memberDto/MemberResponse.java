package Seungmin.Game.domain.member.memberDto;

import Seungmin.Game.common.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberResponse {

    private String loginId;
    private String name;
    private String nickname;
    private Gender gender;
    private LocalDate birthday;
    private LocalDateTime createdDate;


}
