package Seungmin.Game.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((authorizerRequests) ->
                        authorizerRequests
                                .requestMatchers("/user").authenticated()   // 인가 x / 인증 o
                                .requestMatchers("/admin").hasRole("admin") // 권한?
                                .anyRequest().permitAll())                 // 그 외의 사이트 다 허용
                .formLogin((formLogin) ->
                        formLogin
                                .usernameParameter("userId")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/user", true))
                .logout((logout) ->
                        logout
                                .logoutSuccessUrl("/"))
                .csrf(AbstractHttpConfigurer::disable).build();
    }
}
