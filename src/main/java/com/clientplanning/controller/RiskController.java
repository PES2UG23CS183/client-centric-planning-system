package com.clientplanning.controller;

import com.clientplanning.model.User;
import com.clientplanning.service.RiskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ESHWAR (PES2UG23CS188)
 * Handles all /risks routes
 */
@Controller
@RequestMapping("/risks")
public class RiskController {

    @Autowired
    private RiskService riskService;

    private User getSessionUser(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }

    @GetMapping
    public String listAllRisks(HttpSession session, Model model) {
        if (getSessionUser(session) == null) return "redirect:/login";
        model.addAttribute("risks", riskService.getAllRisks());
        model.addAttribute("user", getSessionUser(session));
        return "risk/risk-list";
    }

    @GetMapping("/plan/{planId}")
    public String viewRisksForPlan(@PathVariable Long planId,
                                   HttpSession session, Model model) {
        if (getSessionUser(session) == null) return "redirect:/login";
        model.addAttribute("risks", riskService.getRisksByPlan(planId));
        model.addAttribute("planId", planId);
        model.addAttribute("user", getSessionUser(session));
        return "risk/risk-plan";
    }

    @PostMapping("/flag/{planId}")
    public String flagRisks(@PathVariable Long planId, RedirectAttributes ra) {
        try {
            riskService.flagRisksForPlan(planId);
            ra.addFlashAttribute("success", "Risks flagged successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/risks/plan/" + planId;
    }

    @PostMapping("/add/{planId}")
    public String addRisk(@PathVariable Long planId,
                          @RequestParam String description,
                          @RequestParam String severity,
                          @RequestParam String category,
                          RedirectAttributes ra) {
        try {
            riskService.addManualRisk(planId, description, severity, category);
            ra.addFlashAttribute("success", "Risk added.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/risks/plan/" + planId;
    }

    @PostMapping("/delete/{id}")
    public String deleteRisk(@PathVariable Long id,
                             @RequestParam Long planId,
                             RedirectAttributes ra) {
        riskService.deleteRisk(id);
        ra.addFlashAttribute("success", "Risk deleted.");
        return "redirect:/risks/plan/" + planId;
    }
}
