package com.clientplanning.controller;

import com.clientplanning.model.User;
import com.clientplanning.service.AlignmentService;
import com.clientplanning.service.PlanningRecordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/alignment")
public class AlignmentController {

    @Autowired
    private AlignmentService alignmentService;

    @Autowired
    private PlanningRecordService recordService;

    private User getSessionUser(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }

    @GetMapping
    public String listAlignments(HttpSession session, Model model) {
        if (getSessionUser(session) == null) return "redirect:/login";
        model.addAttribute("alignments", alignmentService.getAllAlignments());
        model.addAttribute("user", getSessionUser(session));
        return "alignment/alignment-list";
    }

    @GetMapping("/plan/{planId}")
    public String viewAlignment(@PathVariable Long planId,
                                HttpSession session, Model model) {
        if (getSessionUser(session) == null) return "redirect:/login";
        model.addAttribute("alignment",
                alignmentService.getAlignmentByPlanId(planId).orElse(null));
        model.addAttribute("planId", planId);
        model.addAttribute("user", getSessionUser(session));
        return "alignment/alignment-view";
    }

    // Calculate score and auto-save a planning record snapshot
    @PostMapping("/calculate/{planId}")
    public String calculateAlignment(@PathVariable Long planId, RedirectAttributes ra) {
        try {
            alignmentService.calculateAlignment(planId);
            recordService.storeRecord(planId);
            ra.addFlashAttribute("success", "Alignment score calculated and record saved.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/alignment/plan/" + planId;
    }
}
