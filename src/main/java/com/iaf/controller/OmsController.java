package com.iaf.controller;

import com.iaf.model.SearchParam;
import com.iaf.service.AnalysisService;
import com.iaf.service.OmsNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class OmsController {

    private final OmsNotificationService omsNotificationService;
    private final AnalysisService analysisService;

    public OmsController(OmsNotificationService omsNotificationService, AnalysisService analysisService) {
        this.omsNotificationService = omsNotificationService;
        this.analysisService = analysisService;
    }

    @GetMapping("/omsNotification")
    public String omsHistory(@ModelAttribute SearchParam searchParam, Model model) {
        model.addAttribute("clientList", analysisService.getClientList());
        if (searchParam.getSearch() != null) {
            int page = searchParam.getPage();
            int totalCount = omsNotificationService.countOmsNotification(searchParam);
            int totalPages = (int) Math.ceil((double) totalCount / searchParam.getPageSize());
            int startPage = Math.max(1, page - 2);
            int endPage = Math.min(totalPages, startPage + 4);
            startPage = Math.max(1, endPage - 4);
            model.addAttribute("historyList", omsNotificationService.getOmsNotification(searchParam));
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", page);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        }
        model.addAttribute("searchParam", searchParam);
        return "omsNotification";
    }

    @GetMapping("/oms/sendAll")
    @ResponseBody
    public ResponseEntity<String> sendNotification(@RequestParam String baseDate) {
        omsNotificationService.sendNotification(baseDate);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/oms/resendOne")
    @ResponseBody
    public ResponseEntity<Map<String, String>> resend(@ModelAttribute SearchParam searchParam) {
        try {
            omsNotificationService.resendNotification(searchParam);
            return ResponseEntity.ok(Map.of("result", "OK"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("result", "FAIL", "message", e.getMessage()));
        }
    }
}
