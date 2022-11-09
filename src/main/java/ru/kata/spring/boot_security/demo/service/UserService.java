package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    User findUserById(long id);

    void saveUser(User user, String[] roles);

    void updateUser(User user, String[] roles);

    void deleteUser(long id);

    UserDetails loadUserByUsername(String username);
}
