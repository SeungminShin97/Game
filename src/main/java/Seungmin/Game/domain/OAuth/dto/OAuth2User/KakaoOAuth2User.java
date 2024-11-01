package Seungmin.Game.domain.OAuth.dto.OAuth2User;

import Seungmin.Game.domain.OAuth.dto.OAuth2UserInfo;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class KakaoOAuth2User extends OAuth2UserInfo {

    public KakaoOAuth2User(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getEmail() {
        return Optional.ofNullable(getAttributes().get("kakao_account"))
                .filter(Map.class::isInstance)
                .map(properties -> ((Map<?,?>) properties).get("email"))
                .map(Objects::toString)
                .orElse(null);
    }

    @Override
    public String getUserIdentifier() {
        return getAttributes().get("id").toString();
    }

    @Override
    public String getName() {
//        원래 코드
//        Map<String, Object> properties = (Map<String, Object>) getAttributes().get("properties");
//        return properties != null ? properties.get("nickname").toString() : null;
        return Optional.ofNullable(getAttributes().get("properties"))
                .filter(Map.class::isInstance)
                .map(properties -> ((Map<?,?>) properties).get("nickname"))
                .map(Objects::toString)
                .orElse(null);
    }
}

