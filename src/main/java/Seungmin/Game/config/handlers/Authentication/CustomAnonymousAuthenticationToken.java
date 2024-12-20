package Seungmin.Game.config.handlers.Authentication;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAnonymousAuthenticationToken extends AnonymousAuthenticationToken {

    private final String anonymousId;

    public CustomAnonymousAuthenticationToken(String key, Object principal,
                                              Collection<? extends GrantedAuthority> authorities,
                                              String anonymousId) {
        super(key, principal, authorities);
        this.anonymousId = anonymousId;
    }

    public String getAnonymousId() {
        return anonymousId;
    }
}
