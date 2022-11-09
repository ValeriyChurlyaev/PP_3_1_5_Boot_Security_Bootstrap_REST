package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;


    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping
    public String getAllUsers(ModelMap model,
                              @AuthenticationPrincipal User user) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", user);
        model.addAttribute("role", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "users";
    }


    @PostMapping("/saveUser")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "select_role", required = false) String[] roles) {
        userService.saveUser(user, roles);
        return "redirect:/admin";
    }

    @PostMapping(value = "/update/{id}")
    public String update(@ModelAttribute("user") User user,
                         ModelMap model,
                         @RequestParam(value = "listRoles", required = false) String[] roles) {

        model.addAttribute("user", user);
        model.addAttribute("role", roleService.getAllRoles());
        userService.updateUser(user, roles);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
