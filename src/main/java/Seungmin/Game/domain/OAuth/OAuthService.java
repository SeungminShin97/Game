package Seungmin.Game.domain.OAuth;

import Seungmin.Game.common.enums.Provider;
import Seungmin.Game.common.enums.Role;
import Seungmin.Game.domain.OAuth.dto.MemberPrincipal;
import Seungmin.Game.domain.OAuth.dto.OAuth2UserInfo;
import Seungmin.Game.domain.OAuth.dto.OAuth2UserInfoFactory;
import Seungmin.Game.domain.member.MemberRepository;
import Seungmin.Game.domain.member.memberDto.Member;
import Seungmin.Game.domain.member.memberDto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Provider provider = Provider.from(registrationId);

        // 소셜 쪽에서 전달받은 값들을 Map 형태로 받음
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, attributes);

        MemberResponse memberResponse = getMember(oAuth2UserInfo, provider);

        return new MemberPrincipal(memberResponse, attributes, userNameAttributeName);
    }

    @Transactional
    private MemberResponse getMember(OAuth2UserInfo oAuth2UserInfo, Provider provider) {
        String identifier = oAuth2UserInfo.getUserIdentifier();
        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();
        Optional<Member> member = memberRepository.findByIdentifierAndProvider(identifier, provider);
        if(member.isEmpty()) {
            Member newMember = Member.builder()
                    .loginId(email)
                    .password(getRandomPassword())
                    .name(name)
                    .nickname(email)
                    .identifier(identifier)
                    .email(email)
                    .provider(provider)
                    .role(Role.Not_registered)
                    .build();
            memberRepository.save(newMember);
            return newMember.toDto();
        }
        return member.get().toDto();
    }

    private String getRandomPassword() {
        return UUID.randomUUID().toString();
    }

//    public String getKakaoAccessToken(String code) throws JsonProcessingException {
//        // HTTP Header
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        // HTTP Body
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "authorization_code");
//        body.add("client_id", clientId);
//        body.add("redirect_uri", redirectUri);
//        body.add("code", code);
//        body.add("client_secret", clientSecret);
//
//        // HTTP request
//        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.exchange(
//                "https://kauth.kakao.com/oauth/token",
//                HttpMethod.POST,
//                kakaoTokenRequest,
//                String.class
//        );
//
//        // HTTP response (JSON) -> 엑세스 토큰 파싱
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//
//        return jsonNode.get("access_token").asText();
//    }
//
//    public OAuthMember getKakaoOAuthMemberInfo(String accessToken) throws JsonProcessingException {
//        // HTTP Header
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        // HTTP request
//        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.exchange(
//                "https://kapi.kakao.com/v2/user/me",
//                HttpMethod.POST,
//                kakaoUserInfoRequest,
//                String.class
//        );
//
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//
//        Long id = jsonNode.get("id").asLong();
//        String email = jsonNode.get("kakao_account").get("email").asText();
//        int idx = email.indexOf("@");
//        String loginId = email.substring(0, idx);
//        String name = jsonNode.get("properties").get("nickname").asText();
//
//        return OAuthMember.builder()
//                .id(id)
//                .loginId(loginId)
//                .name(name)
//                .email(email).build();
//    }
}
