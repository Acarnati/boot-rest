package jm.service;

import jm.dao.UserDao;
import jm.model.Role;
import jm.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

public interface UserService extends UserDetailsService {
    User findByLogin(String login);
    void addNewUser(User user);
    Set<User> findAllUsers();
    User findUserById(long id);
    void deleteUserById(long id);
    void updateUser(User user);
    Role getRole(String role);
}
