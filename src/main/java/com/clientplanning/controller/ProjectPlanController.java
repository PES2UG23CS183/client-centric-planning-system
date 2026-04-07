package com.clientplanning.controller;

import com.clientplanning.model.User;
import com.clientplanning.service.PlanningEngineService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/plans")
public class ProjectPlanController {

    @Autowired
    private PlanningEngineService planningEngineService;

    private User getSessionUser(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }

    @GetMapping
    public String listPlans(HttpSession session, Model model) {
        if (getSessionUser(session) == null) return "redirect:/login";
        model.addAttribute("plans", planningEngineService.getAllPlans());
        model.addAttribute("user", getSessionUser(session));
        return "plan/plan-list";
    }

    // Triggers the PlanningEngine to generate a plan for the given profile
    @GetMapping("/generate/{profileId}")
    public String generatePlan(@PathVariable Long profileId,
                               HttpSession session,
                               RedirectAttributes ra) {
        if (getSessionUser(session) == null) return "redirect:/login";
        try {
            var plan = planningEngineService.generateProjectPlan(profileId);
            ra.addFlashAttribute("success", "Project plan generated successfully.");
            return "redirect:/plans/" + plan.getPlanId();
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/profiles";
        }
    }

    @GetMapping("/{id}")
    public String viewPlan(@PathVariable Long id, HttpSession session, Model model) {
        if (getSessionUser(session) == null) return "redirect:/login";
        return planningEngineService.getPlanById(id).map(plan -> {
            model.addAttribute("plan", plan);
            model.addAttribute("user", getSessionUser(session));
            return "plan/plan-view";
        }).orElse("redirect:/plans");
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               RedirectAttributes ra) {
        try {
            planningEngineService.updateStatus(id, status);
            ra.addFlashAttribute("success", "Status updated.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/plans/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deletePlan(@PathVariable Long id, RedirectAttributes ra) {
        planningEngineService.deletePlan(id);
        ra.addFlashAttribute("success", "Plan deleted.");
        return "redirect:/plans";
    }
}
