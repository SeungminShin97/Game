package Seungmin.Game.common;

import Seungmin.Game.common.dto.NotificationDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {

    @GetMapping("/common/messageRedirect")
    private String messageRedirect(HttpSession session, Model model) {
        NotificationDto params = (NotificationDto) session.getAttribute("params");
        model.addAttribute("params", params);
        session.removeAttribute("params");
        return "common/messageRedirect";
    }

}
