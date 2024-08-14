package Seungmin.Game.domain.OAuth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthMember {
    private Long id;
    private String loginId;
    private String name;
    private String email;
}
