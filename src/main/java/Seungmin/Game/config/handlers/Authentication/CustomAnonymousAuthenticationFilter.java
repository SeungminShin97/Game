package Seungmin.Game.config.handlers.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import java.util.UUID;

public class CustomAnonymousAuthenticationFilter extends AnonymousAuthenticationFilter {

    private final String key;

    public CustomAnonymousAuthenticationFilter(String key) {
        super(key);
        this.key = key;
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {
        String anonymousId = UUID.randomUUID().toString();

        return new CustomAnonymousAuthenticationToken(this.key, "anonymousUser", getAuthorities(), anonymousId);
    }
}
