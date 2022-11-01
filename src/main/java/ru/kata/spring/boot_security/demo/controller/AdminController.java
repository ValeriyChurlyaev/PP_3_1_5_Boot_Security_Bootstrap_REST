package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping
    public String getAllUsers(ModelMap model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }


    @GetMapping("/new")
    public String addUser(ModelMap model) {
        model.addAttribute("user", new User());
        model.addAttribute("role", roleService.getAllRoles());
        return "new";
    }

    @PostMapping("/saveUser")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "select_role", required = false) String[] roles) {
        List<Role> role = new ArrayList<>();
        role.add(roleService.getAllRoles().get(1));
        for (String s : roles) {
            if (s.equals("ROLE_ADMIN")) {
                role.add(roleService.getAllRoles().get(0));
            }
        }
        user.setRoles(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String update(ModelMap model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("role", roleService.getAllRoles());
        return "update";
    }

    @PostMapping(value = "/update/{id}")
    public String update(long id, User user,
                         @RequestParam(value = "select_role", required = false) String[] roles) {
        List<Role> role = new ArrayList<>();
        role.add(roleService.getAllRoles().get(1));
        for (String s : roles) {
            if (s.equals("ROLE_ADMIN")) {
                role.add(roleService.getAllRoles().get(0));
            }
        }
        user.setRoles(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.updateUser(id, user);
        return "redirect:/admin";
    }


    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
