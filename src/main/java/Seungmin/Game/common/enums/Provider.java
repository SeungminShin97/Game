package Seungmin.Game.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum Provider {
    KAKAO("kakao_account", "id", "email"),
    GOOGLE(null, "sub", "email");

    private final String attributeKey;
    private final String providerCode;
    private final String identifier;

    public static Provider from(String provider) {
        String upperCastedProvider = provider.toUpperCase();

        return Arrays.stream(Provider.values())
                .filter(info -> info.name().equals(upperCastedProvider))
                .findFirst()
                .orElseThrow();
    }
}
