package Seungmin.Game.config;

import Seungmin.Game.common.enums.Role;
import Seungmin.Game.config.handlers.CustomAuthenticationFailureHandler;
import Seungmin.Game.config.handlers.CustomAuthenticationSuccessHandler;
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
                                .requestMatchers("/admin").hasRole(Role.Admin.name()) // 권한?
                                .anyRequest().permitAll())                 // 그 외의 사이트 다 허용
                .formLogin((formLogin) ->
                        formLogin
                                .loginPage("/member")
                                .usernameParameter("loginId")
                                .passwordParameter("password")
                                .loginProcessingUrl("/member/login")
//                                .defaultSuccessUrl("/", true)   // 로그인 성공 시 이동할 페이지
                                .successHandler(customAuthenticationSuccessHandler)
                                .failureHandler(customAuthenticationFailureHandler))
                .logout((logout) ->
                        logout          // logout을 하면 logoutfilter 생성 -> 자동으로 로그아웃 핸들러
                                .logoutUrl("/member/logout")
                                .logoutSuccessUrl("/")
                                .deleteCookies("JSESSIONID"))
                .csrf(AbstractHttpConfigurer::disable).build();
    }
}
