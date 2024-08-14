package Seungmin.Game.config;

import Seungmin.Game.interceptor.AuthenticationInterceptor;
import Seungmin.Game.interceptor.LogInterceptor;
import Seungmin.Game.interceptor.UserInfoInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;
    private final UserInfoInterceptor userInfoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 로그 인터셉터
        registry.addInterceptor(new LogInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**");

        // 게시물 인증 인터셉터
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/post/**", "/comment/**")
                .excludePathPatterns("/post/list", "/post/view/**", "/comment/list/**");

        // 로그인 유저 정보 전달
        registry.addInterceptor(userInfoInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/oauth/kakao/callback");
    }
}
