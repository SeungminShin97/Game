package Seungmin.Game.domain.OAuth.dto;

import Seungmin.Game.common.enums.Provider;
import Seungmin.Game.domain.OAuth.dto.OAuth2User.GoogleOAuth2User;
import Seungmin.Game.domain.OAuth.dto.OAuth2User.KakaoOAuth2User;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(Provider provider, Map<String, Object> attributes) {
        switch (provider) {
            case GOOGLE -> {
                return new GoogleOAuth2User(attributes);
            }
            case KAKAO -> {
                return new KakaoOAuth2User(attributes);
            }
        }
        throw new OAuth2AuthenticationException("INVALID PROVIDER TYPE");
    }
}
