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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/user")
    public ResponseEntity<User> getOurUser() {
        User user = userService.authUser();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        User user = userService.findUserById(id);
        if (user == null) {
            throw new NoSuchUserException("There is no user with ID = " + id + " in Database");
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserIncorrectData> addNewUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = "Error adding a user";
            return new ResponseEntity<>(new UserIncorrectData(error), HttpStatus.BAD_REQUEST);
        }
        userService.addNewUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserIncorrectData> updateUser(@PathVariable("id") Long id, @Valid @RequestBody User user) {
        try {
            userService.updateUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            throw new NoSuchUserException("User with such login Exist");
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        User user = userService.findUserById(id);
        if (user == null) {
            throw new NoSuchUserException("There is no user with ID = " + id + " in Database");
        }
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/roles")
    public ResponseEntity<Set<Role>> findAllRoles() {
        Set<Role> roles = roleService.getAllRole();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
