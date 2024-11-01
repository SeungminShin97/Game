package Seungmin.Game.domain.OAuth.dto.OAuth2User;


import Seungmin.Game.domain.OAuth.dto.OAuth2UserInfo;

import java.util.Map;

public class GoogleOAuth2User extends OAuth2UserInfo {



    public GoogleOAuth2User(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getEmail() {
        return getAttributes().get("email").toString();
    }

    @Override
    public String getUserIdentifier() {
        return getAttributes().get("sub").toString();
    }

    @Override
    public String getName() {
        return getAttributes().get("name").toString();
    }
}
