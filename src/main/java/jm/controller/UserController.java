package jm.controller;
import jm.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@Controller
@RequestMapping
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("login")
    public String loginPage() {
        return "pages/login";
    }

    @GetMapping(value = "/index")
    public String printWelcome(ModelMap model, Principal principal) {
        model.addAttribute("ourUser", userService.loadUserByUsername(principal.getName()));
        return "pages/index";
    }

    @GetMapping(value = "/userPage")
    public String printWelcomeUser(ModelMap model, Principal principal) {
        model.addAttribute("ourUser", userService.loadUserByUsername(principal.getName()));
        return "pages/userPage";
    }
}
