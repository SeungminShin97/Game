package Seungmin.Game.interceptor;

import Seungmin.Game.domain.member.MemberService;
import Seungmin.Game.domain.member.memberDto.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
public class UserInfoInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            try{
                Member member = memberService.getMemberByAuthentication(authentication);
                String nickname = member.getNickname();
                modelAndView.addObject("loginUser", nickname);
            } catch(UsernameNotFoundException e){
                modelAndView.addObject("loginUser", null);
            }
        }
    }
}
