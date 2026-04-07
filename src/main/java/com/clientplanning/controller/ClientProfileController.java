package com.clientplanning.controller;

import com.clientplanning.model.*;
import com.clientplanning.service.ClientProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * DHANYA (PES2UG23CS169)
 * Handles all /profiles routes - list, create, view, edit, delete, preferences
 */
@Controller
@RequestMapping("/profiles")
public class ClientProfileController {

    @Autowired
    private ClientProfileService profileService;

    private User getSessionUser(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }

    @GetMapping
    public String listProfiles(HttpSession session, Model model) {
        if (getSessionUser(session) == null) return "redirect:/login";
        model.addAttribute("profiles", profileService.getAllProfiles());
        model.addAttribute("user", getSessionUser(session));
        return "client/profile-list";
    }

    @GetMapping("/new")
    public String newProfileForm(HttpSession session, Model model) {
        if (getSessionUser(session) == null) return "redirect:/login";
        model.addAttribute("clients", profileService.getAllClients());
        model.addAttribute("user", getSessionUser(session));
        return "client/profile-form";
    }

    @PostMapping("/new")
    public String createProfile(@RequestParam Long clientId,
                                @RequestParam String workingStyle,
                                @RequestParam String communication,
                                @RequestParam String reviewFrequency,
                                @RequestParam(required = false) String constraint,
                                @RequestParam String budgetRange,
                                @RequestParam int requirementClarity,
                                @RequestParam String riskTolerance,
                                RedirectAttributes ra) {
        try {
            ClientProfile profile = profileService.createProfile(clientId, workingStyle,
                    communication, reviewFrequency, constraint, budgetRange,
                    requirementClarity, riskTolerance);
            ra.addFlashAttribute("success", "Profile created successfully.");
            return "redirect:/profiles/" + profile.getProfileId();
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/profiles/new";
        }
    }

    @GetMapping("/{id}")
    public String viewProfile(@PathVariable Long id, HttpSession session, Model model) {
        if (getSessionUser(session) == null) return "redirect:/login";
        return profileService.getProfileById(id).map(profile -> {
            model.addAttribute("profile", profile);
            model.addAttribute("preference", profileService.getPreferenceByProfileId(id).orElse(null));
            model.addAttribute("user", getSessionUser(session));
            return "client/profile-view";
        }).orElse("redirect:/profiles");
    }

    @GetMapping("/edit/{id}")
    public String editProfileForm(@PathVariable Long id, HttpSession session, Model model) {
        if (getSessionUser(session) == null) return "redirect:/login";
        return profileService.getProfileById(id).map(profile -> {
            model.addAttribute("profile", profile);
            model.addAttribute("clients", profileService.getAllClients());
            model.addAttribute("user", getSessionUser(session));
            return "client/profile-edit";
        }).orElse("redirect:/profiles");
    }

    @PostMapping("/edit/{id}")
    public String updateProfile(@PathVariable Long id,
                                @RequestParam String workingStyle,
                                @RequestParam String communication,
                                @RequestParam String reviewFrequency,
                                @RequestParam(required = false) String constraint,
                                @RequestParam String budgetRange,
                                @RequestParam int requirementClarity,
                                @RequestParam String riskTolerance,
                                RedirectAttributes ra) {
        try {
            profileService.updateProfile(id, workingStyle, communication, reviewFrequency,
                    constraint, budgetRange, requirementClarity, riskTolerance);
            ra.addFlashAttribute("success", "Profile updated.");
            return "redirect:/profiles/" + id;
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/profiles/edit/" + id;
        }
    }

    @PostMapping("/{id}/preference")
    public String savePreference(@PathVariable Long id,
                                 @RequestParam String developmentApproach,
                                 @RequestParam String executionStrategy,
                                 @RequestParam String reviewProcess,
                                 RedirectAttributes ra) {
        try {
            profileService.savePreference(id, developmentApproach, executionStrategy, reviewProcess);
            ra.addFlashAttribute("success", "Preferences saved.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profiles/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteProfile(@PathVariable Long id, RedirectAttributes ra) {
        profileService.deleteProfile(id);
        ra.addFlashAttribute("success", "Profile deleted.");
        return "redirect:/profiles";
    }
}
