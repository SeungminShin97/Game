package Seungmin.Game.domain.member.memberDto;

import Seungmin.Game.common.BaseTimeEntity;
import Seungmin.Game.common.enums.Gender;
import Seungmin.Game.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Entity(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
@Getter
//@Builder
public class Member extends BaseTimeEntity implements UserDetails {
    //  birthday, deleteYn, createdDate, modifiedDate

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "loginId", nullable = false, unique = true)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "role")
    private Role role = Role.User;

    // todo 주소도 해볼까요? (주소 검색하는 것도)


    @Builder
    public Member (String loginId, String password, String name, String nickname, Gender gender, LocalDate birthday) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.birthday = birthday;
    }

    public MemberResponse toDto() {
        return MemberResponse.builder()
                .loginId(loginId)
                .name(name)
                .nickname(nickname)
                .gender(gender)
                .birthday(birthday)
                .createdDate(createdDate)
                .build();
    }

    // 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return Collections.singletonList(role); }

    // 사용자의 id 반환
    @Override
    public String getUsername() {
        return loginId;
    }

    // 계정 만료 여부 반환
    // false: 잠김
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 여부 반환
    // false: 잠김
    @Override
    public boolean isAccountNonLocked() { return true; }

    // 인증 정보 만료 여부 반환
    // false: 비밀번호 만료
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부 반환
    // false: 비활성화
    @Override
    public boolean isEnabled() {
        return true;
    }
}
