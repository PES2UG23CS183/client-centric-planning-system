package com.clientplanning.controller;

import com.clientplanning.model.User;
import com.clientplanning.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * DELISHA (PES2UG23CS166)
 
 */
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) return "redirect:/dashboard";
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes ra) {
        return userService.login(email, password).map(user -> {
            session.setAttribute("loggedInUser", user);
            return "redirect:/dashboard";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "Invalid email or password.");
            return "redirect:/login";
        });
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String name,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam String role,
                                  @RequestParam(required = false) String organisation,
                                  @RequestParam(required = false) String contactInfo,
                                  @RequestParam(required = false) String specialisation,
                                  @RequestParam(defaultValue = "0") int experience,
                                  RedirectAttributes ra) {
        try {
            userService.register(name, email, password, role,
                    organisation, contactInfo, specialisation, experience);
            ra.addFlashAttribute("success", "Account created! Please log in.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        model.addAttribute("totalClients", userService.getAllClients().size());
        model.addAttribute("totalAnalysts", userService.getAllAnalysts().size());
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        return "auth/dashboard";
    }
}
