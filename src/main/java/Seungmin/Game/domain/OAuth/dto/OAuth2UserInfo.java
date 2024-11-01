package Seungmin.Game.domain.OAuth.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class OAuth2UserInfo {

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    private final Map<String, Object> attributes;

    public abstract String getEmail();
    public abstract String getUserIdentifier();
    public abstract String getName();
}
