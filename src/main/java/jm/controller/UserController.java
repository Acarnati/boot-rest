package jm.controller;

import jm.model.Role;
import jm.model.User;
import jm.service.RoleService;
import jm.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping
public class UserController {
    private UserService userService;
    private RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("login")
    public String loginPage() {
        return "pages/login";
    }

    @GetMapping("index")
    public String printWelcome(ModelMap model, Principal principal) {
        model.addAttribute("users", userService.findAllUsers());
        User user = new User();
        List<Role> roles = roleService.getAllRole();
        model.addAttribute("roles", roles);
        model.addAttribute("user", user);
        model.addAttribute("ourUser", userService.loadUserByUsername(principal.getName()));
        return "pages/index";
    }
}
