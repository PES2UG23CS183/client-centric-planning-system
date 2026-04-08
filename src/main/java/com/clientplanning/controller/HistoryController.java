package com.clientplanning.controller;

import com.clientplanning.model.User;
import com.clientplanning.service.PlanningRecordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ESHWAR (PES2UG23CS188)
 * Handles all /history routes
 */
@Controller
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private PlanningRecordService recordService;

    private User getSessionUser(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }

    @GetMapping
    public String listHistory(HttpSession session, Model model) {
        if (getSessionUser(session) == null) return "redirect:/login";
        model.addAttribute("records", recordService.getAllRecords());
        model.addAttribute("user", getSessionUser(session));
        return "history/history-list";
    }

    @GetMapping("/plan/{planId}")
    public String historyForPlan(@PathVariable Long planId,
                                 HttpSession session, Model model) {
        if (getSessionUser(session) == null) return "redirect:/login";
        model.addAttribute("records", recordService.getRecordsByPlan(planId));
        model.addAttribute("planId", planId);
        model.addAttribute("user", getSessionUser(session));
        return "history/history-plan";
    }

    @PostMapping("/delete/{id}")
    public String deleteRecord(@PathVariable Long id,
                               @RequestParam Long planId,
                               RedirectAttributes ra) {
        recordService.deleteRecord(id);
        ra.addFlashAttribute("success", "Record deleted.");
        return "redirect:/history/plan/" + planId;
    }
}
