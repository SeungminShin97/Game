package Seungmin.Game.common.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    Admin,
    User;

    @Override
    public String getAuthority() {
        return name();
    }
}
