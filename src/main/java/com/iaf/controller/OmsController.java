package com.iaf.controller;

import com.iaf.model.SearchParam;
import com.iaf.model.OmsNotificationHistory;
import com.iaf.service.AnalysisService;
import com.iaf.service.OmsNotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class OmsController {

    private final OmsNotificationService omsNotificationService;
    private final AnalysisService analysisService;

    public OmsController(OmsNotificationService omsNotificationService, AnalysisService analysisService) {
        this.omsNotificationService = omsNotificationService;
        this.analysisService = analysisService;
    }

    @GetMapping("/oms-history")
    public String omsHistory(@ModelAttribute SearchParam searchParam, Model model) {
        model.addAttribute("clientList", analysisService.getClientList());
        if (searchParam.getSearch() != null) {
            int page = searchParam.getPage();
            int totalCount = omsNotificationService.countOmsNotificationHistory(searchParam);
            int totalPages = (int) Math.ceil((double) totalCount / searchParam.getPageSize());
            int startPage = Math.max(1, page - 2);
            int endPage = Math.min(totalPages, startPage + 4);
            startPage = Math.max(1, endPage - 4);
            model.addAttribute("historyList", omsNotificationService.getOmsNotificationHistory(searchParam));
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", page);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        }
        model.addAttribute("searchParam", searchParam);
        return "omsNotificationHistory";
    }
}
