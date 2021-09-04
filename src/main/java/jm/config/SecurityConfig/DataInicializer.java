package jm.config.SecurityConfig;

import jm.model.Role;
import jm.model.User;
import jm.service.RoleService;
import jm.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataInicializer {
    private final UserService userService;
    private final RoleService roleService;

    public DataInicializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void saveUser() {
        Role role1 = new Role("ADMIN");
        Role role2 = new Role("USER");
        roleService.createRole(role1);
        roleService.createRole(role2);
        User user1 = new User("admin@mail.ru", "1", "Иван", "Иванов", (byte) 29,role1, role2);
        userService.addNewUser(user1);
        User user2 = new User("user@mail.ru", "2", "Ирина", "Лапина", (byte) 35, role2);
        userService.addNewUser(user2);
    }
}