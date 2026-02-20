package com.iaf.controller;

import com.iaf.model.SearchParam;
import com.iaf.scheduler.AnalysisScheduler;
import com.iaf.service.AnalysisService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AnalysisController {

    private final AnalysisService analysisService;
    private final AnalysisScheduler analysisScheduler;

    public AnalysisController(AnalysisService analysisService, AnalysisScheduler analysisScheduler) {
        this.analysisService = analysisService;
        this.analysisScheduler = analysisScheduler;
    }

    @GetMapping("/analysis")
    public String analysis(@ModelAttribute SearchParam searchParam, Model model) {
        model.addAttribute("clientList", analysisService.getClientList());
        if (searchParam.getClientId() != null) {
            model.addAttribute("categoryList", analysisService.getCategoryListByClientId(searchParam.getClientId()));
        }
        if (searchParam.getSearch() != null) {
            int page = searchParam.getPage();
            int totalCount = analysisService.countAnalysisResult(searchParam);
            int totalPages = (int) Math.ceil((double) totalCount / searchParam.getPageSize());
            int startPage = Math.max(1, page - 2);
            int endPage = Math.min(totalPages, startPage + 4);
            startPage = Math.max(1, endPage - 4);
            model.addAttribute("analysisList", analysisService.getAnalysisResult(searchParam));
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", page);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        }
        model.addAttribute("searchParam", searchParam);
        return "analysis";
    }

    @GetMapping("/analysis/categories")
    @ResponseBody
    public List<String> getCategoriesByClient(@RequestParam Long clientId) {
        return analysisService.getCategoryListByClientId(clientId);
    }

    @GetMapping("/analysis/test")
    @ResponseBody
    public String testAnalysis(@RequestParam String baseDate) {
        analysisScheduler.runAnalysis(baseDate);
        return "OK";
    }
}
