package Seungmin.Game.domain.user;

import Seungmin.Game.common.dto.NotificationDto;
import Seungmin.Game.domain.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final MemberService memberService;

    private String showMessageAndRedirect(final NotificationDto params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }

    @GetMapping("/user")
    public String goUserPage(Model model, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
                NotificationDto notificationDto = NotificationDto.builder()
                        .message("로그인 후 이용해 주세요")
                        .redirectUri("/")
                        .method(RequestMethod.GET)
                        .build();
                return showMessageAndRedirect(notificationDto, model);
            }
            String loginId = memberService.getMemberByAuthentication(authentication).getLoginId();
            model.addAttribute("loginId", loginId);
            return "member/user";
        } catch (Exception e) {
            NotificationDto notificationDto = NotificationDto.builder()
                    .message(e.getMessage())
                    .redirectUri("/")
                    .method(RequestMethod.GET)
                    .data(null).build();
            return showMessageAndRedirect(notificationDto, model);
        }
    }
}
