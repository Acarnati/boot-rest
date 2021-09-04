package jm.controller;

import jm.ExeptionHandler.NoSuchUserException;
import jm.model.Role;
import jm.model.User;
import jm.service.RoleService;
import jm.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class RestUserController {
    private UserService userService;
    private RoleService roleService;

    public RestUserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public ResponseEntity<Set<User>> apiGetAllUsers() {
        Set<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        User user = userService.findUserById(id);
        if (user == null) {
            throw new NoSuchUserException("There is no user with ID = " + id + " in Database");
        }
        return user;
    }

    @PostMapping("/users")
    public User addNewUser(@RequestBody User user) {
        Set<Role> roles = new HashSet<>();
        for (Role role: user.getRoles()) {
            roles.add(roleService.getRoleByRolename(role.getRole()));
        }
        user.setRoles(roles);
        userService.addNewUser(user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        Set<Role> roles = new HashSet<>();
        for (Role role: user.getRoles()) {
            roles.add(roleService.getRoleByRolename(role.getRole()));
        }
        user.setRoles(roles);
        userService.updateUser(user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable int id) {
        User user = userService.findUserById(id);
        if (user == null) {
            throw new NoSuchUserException("There is no user with ID = " + id + " in Database");
        }
        userService.deleteUserById(id);
        return "User with ID = " + id + " was deleted";
    }
}
