package jm.controller;

import jm.model.Role;
import jm.model.User;
import jm.service.RoleService;
import jm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

    @GetMapping(value = "/index")
    public String printWelcome(ModelMap model, Principal principal) {
        model.addAttribute("users", userService.findAllUsers());
        User user = new User();
        Set<Role> roles = roleService.getAllRole();
        model.addAttribute("roles", roles);
        model.addAttribute("user", user);
        model.addAttribute("ourUser", userService.loadUserByUsername(principal.getName()));
        return "pages/index";
    }

//    @GetMapping(value = "/users/add")
//    public String addUser(@ModelAttribute("user") User user, @RequestParam(name = "rolesSelected",
//            defaultValue = "0") Long[] rolesId) {
//        Set<Role> roles = new HashSet<>();
//        for (Long roleId : rolesId) {
//            roles.add(roleService.getRoleById(roleId));
//        }
//        user.setRoles(roles);
//        userService.addNewUser(user);
//        return "redirect:/index";
//    }
}
