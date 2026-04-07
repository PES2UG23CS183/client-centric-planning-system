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
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private User getSessionUser(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }

    @GetMapping
    public String listUsers(HttpSession session, Model model) {
        if (getSessionUser(session) == null) return "redirect:/login";
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", getSessionUser(session));
        return "auth/users";
    }

    @GetMapping("/clients")
    public String listClients(HttpSession session, Model model) {
        if (getSessionUser(session) == null) return "redirect:/login";
        model.addAttribute("clients", userService.getAllClients());
        model.addAttribute("user", getSessionUser(session));
        return "auth/clients";
    }

    @GetMapping("/analysts")
    public String listAnalysts(HttpSession session, Model model) {
        if (getSessionUser(session) == null) return "redirect:/login";
        model.addAttribute("analysts", userService.getAllAnalysts());
        model.addAttribute("user", getSessionUser(session));
        return "auth/analysts";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
        userService.deleteUser(id);
        ra.addFlashAttribute("success", "User deleted successfully.");
        return "redirect:/users";
    }
}
