package jm.controller;

import jm.ExeptionHandler.NoSuchUserException;
import jm.ExeptionHandler.UserIncorrectData;
import jm.model.Role;
import jm.model.User;
import jm.service.RoleService;
import jm.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public ResponseEntity<UserIncorrectData> addNewUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            String error = getErrorsFromBindingResult(bindingResult);
            return new ResponseEntity<>(new UserIncorrectData(error), HttpStatus.BAD_REQUEST);
        }
        userService.addNewUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String getErrorsFromBindingResult(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.joining("; "));
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        Set<Role> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
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

    @GetMapping(value = "/roles")
    public Set<Role> findAllRoles() {
        return roleService.getAllRole();
    }
}
