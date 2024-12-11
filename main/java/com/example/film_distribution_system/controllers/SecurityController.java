package com.example.film_distribution_system.controllers;

import com.example.film_distribution_system.services.UserService;
import com.example.film_distribution_system.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class SecurityController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String LoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверный логин или пароль");
        }
        return "security/login";
}

    @GetMapping("/signup")
    public String SignUpPage(Model model) {
        model.addAttribute("user", new Users());
        return "security/signup";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute("user") Users user, Model model) {
        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "security/signup";
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }

        if (user.getPassword().length() < 8) {
            model.addAttribute("passwordError", "Пароль должен содержать минимум 8 символов");
            return "security/signup";
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        if (user.getUsername().equalsIgnoreCase("admin")) {
            user.setRoles(Set.of("ROLE_USER", "ROLE_ADMIN"));
        } else {
            user.setRoles(Set.of("ROLE_USER"));
        }

        userService.saveUser(user);
        return "redirect:/login";
    }

}
