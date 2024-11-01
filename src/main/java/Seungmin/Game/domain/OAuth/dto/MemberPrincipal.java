package Seungmin.Game.domain.OAuth.dto;

import Seungmin.Game.domain.member.memberDto.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberPrincipal implements OAuth2User {


    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private MemberResponse memberResponse;
    private Collection<? extends GrantedAuthority> authorities;

    public MemberPrincipal(MemberResponse memberResponse) {
        this.memberResponse = memberResponse;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(memberResponse.getRole().name()));
    }

    public MemberPrincipal(MemberResponse memberResponse, Map<String, Object> attributes, String nameAttributeKey) {
        this.memberResponse = memberResponse;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(memberResponse.getRole().name()));
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
    }

    @Override
    public String getName() {
        return memberResponse.getIdentifier();
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
