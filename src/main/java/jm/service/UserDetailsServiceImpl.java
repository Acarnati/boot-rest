package jm.service;

import jm.ExeptionHandler.NoSuchUserException;
import jm.dao.UserDao;
import jm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsServiceImpl(UserDao userDao, BCryptPasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByLogin(String login) {

        try {
            return userDao.findByLogin(login);
        } catch (Exception e) {
            throw new NoSuchUserException("There is not user with such login");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.findByLogin(s);
    }

    @Override
    public void addNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
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
