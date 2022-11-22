package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;


    @Autowired
    public UserServiceImp(UserDAO userDAO, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public User findUserById(long id) {
        return userDAO.findUserById(id);
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> userRoles = user.getRoles().stream().map(role -> roleService.findRoleById(role.getId()))
                .collect(Collectors.toList());
        user.setRoles(userRoles);
        userDAO.saveUser(user);
    }


    @Override
    @Transactional
    public void updateUser(User newUser, int id) {
        newUser.setId(id);
        User oldUser = userDAO.findUserById(id);
        if (newUser.getRoles().isEmpty()) {
            newUser.setRoles(oldUser.getRoles());
        }
        if (newUser.getPassword().isEmpty()) {

            newUser.setPassword(passwordEncoder.encode(oldUser.getPassword()));
        }
        userDAO.updateUser(newUser);
    }


    @Override
    @Transactional
    public boolean deleteUser(long id) {
        userDAO.deleteUser(id);
        if (userDAO.findUserById(id) == null) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<User> user = userDAO.findUserByEmail(email);
        return user.get(0);
    }
}

