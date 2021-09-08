package jm.service;

import jm.ExeptionHandler.NoSuchUserException;
import jm.dao.RoleDAO;
import jm.dao.UserDao;
import jm.model.Role;
import jm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleDAO roleDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, BCryptPasswordEncoder passwordEncoder, RoleDAO roleDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleDao = roleDao;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.findByLogin(s);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        int size = user.getAuthorities().size();
        return user;
    }

    @Override
    public Role getRole(String role) {
        return roleDao.getRoleByRolename(role);
    }

    @Override
    public void addNewUser(User user) {
        Set<Role> defaultRoles = Collections.singleton(getRole("USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles().size() == 0) {
            user.setRoles(defaultRoles);
        }
        try {
            userDao.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new NoSuchUserException("User with such email exist");
        }
    }

    @Override
    public User authUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            email = ((UserDetails)principal).getUsername();
        } else {
            email = principal.toString();
        }
        return (User) loadUserByUsername(email);
    }

    @Override
    public Set<User> findAllUsers() {
        // sorted by id
        return userDao.findAll().stream()
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public void updateUser(User user) {
        Set<Role> defaultRoles = Collections.singleton(getRole("USER"));
        if (user.getRoles().size() == 0) {
            user.setRoles(defaultRoles);
        }
        User oldUser = findUserById(user.getId());
        if (!oldUser.getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userDao.save(user);
    }

    @Override
    public User findUserById(long id) {
        return userDao.findById(id).orElseThrow(() -> new NoSuchUserException("User with such id does not exist"));
    }

    @Override
    public void deleteUserById(long id) {
        userDao.deleteById(id);
    }
}
