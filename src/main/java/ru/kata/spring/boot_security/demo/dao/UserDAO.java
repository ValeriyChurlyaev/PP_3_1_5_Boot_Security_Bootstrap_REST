package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDAO {

    List<User> getAllUsers();

    User findUserById(long id);

    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(long id);

    List<User> findUserByEmail(String email);

    List<User> findByUsername(String username);
}
