package Seungmin.Game.domain.member.memberDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberChatDto {

    private Long id;
    private String loginId;
    private String nickname;
}
