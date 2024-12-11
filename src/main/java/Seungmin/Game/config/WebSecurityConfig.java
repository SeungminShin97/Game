package Seungmin.Game.config;

import Seungmin.Game.common.enums.Role;
import Seungmin.Game.config.handlers.Authentication.CustomAuthenticationFailureHandler;
import Seungmin.Game.config.handlers.Authentication.CustomAuthenticationSuccessHandler;
import Seungmin.Game.config.handlers.CustomCorsConfigurationSource;
import Seungmin.Game.config.handlers.OAuth2.OAuth2FailureHandler;
import Seungmin.Game.config.handlers.OAuth2.OAuth2SuccessHandler;
import Seungmin.Game.domain.OAuth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final OAuthService oAuthService;

    private final CustomCorsConfigurationSource customCorsConfigurationSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((authorizerRequests) ->
                        authorizerRequests
                                .requestMatchers("/user").authenticated()   // 인가 x / 인증 o
//                                .requestMatchers("/admin").hasRole(Role.Admin.name()) // 권한?
                                .requestMatchers("/admin").hasAuthority(Role.ROLE_ADMIN.name())
                                .anyRequest().permitAll())                 // 그 외의 사이트 다 허용
                .formLogin((formLogin) ->
                        formLogin
                                .loginPage("/member")
                                .usernameParameter("loginId")
                                .passwordParameter("password")
                                .loginProcessingUrl("/member/login")
                                .successHandler(customAuthenticationSuccessHandler)
                                .failureHandler(customAuthenticationFailureHandler))
                .oauth2Login((oAuth2LoginConfigurer) ->
                        oAuth2LoginConfigurer
                                .loginPage("/member")
                                .successHandler(oAuth2SuccessHandler)
                                .failureHandler(oAuth2FailureHandler)
//                                .defaultSuccessUrl("/")
                                .userInfoEndpoint((userInfoEndpointConfig) ->
                                        userInfoEndpointConfig
                                                .userService(oAuthService)
                                        )
                        )
                .logout((logout) ->
                        logout          // logout을 하면 logoutfilter 생성 -> 자동으로 로그아웃 핸들러
                                .logoutUrl("/member/logout")
                                .logoutSuccessUrl("/")
                                .deleteCookies("JSESSIONID"))
                .cors(corsCustomizer -> corsCustomizer
                        .configurationSource(customCorsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
