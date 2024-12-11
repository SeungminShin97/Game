package Seungmin.Game.common.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_NOT_REGISTERED;

    @Override
    public String getAuthority() {
        return name();
    }
}