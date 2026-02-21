package com.iaf.service;

import com.iaf.mapper.AnalysisMapper;
import com.iaf.mapper.ClientMapper;
import com.iaf.model.AnalysisResult;
import com.iaf.model.SearchParam;
import com.iaf.model.Client;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class AnalysisService {

    private final AnalysisMapper analysisMapper;
    private final ClientMapper clientMapper;

    public AnalysisService(AnalysisMapper analysisMapper, ClientMapper clientMapper) {
        this.analysisMapper = analysisMapper;
        this.clientMapper = clientMapper;
    }

    public List<Client> getClientList() {
        return clientMapper.selectClientList();
    }

    public List<String> getCategoryListByClientId(Long clientId) {
        return clientMapper.selectCategoryListByClientId(clientId);
    }

    public int getAlertClientCount(SearchParam param) {
        return analysisMapper.selectAlertClientCount(param);
    }

    public int getOmsSuccessCount(SearchParam param) {
        return analysisMapper.selectOmsSuccessCount(param);
    }

    public Map<String, Object> getStatusSummary(SearchParam param) {
        if (param.getBaseDate() == null || param.getBaseDate().isBlank()) {
            param.setBaseDate(LocalDate.now().toString());
        }
        return analysisMapper.selectStatusSummary(param);
    }

    public int countAnalysisResult(SearchParam param) {
        if (param.getBaseDate() == null || param.getBaseDate().isBlank()) {
            param.setBaseDate(LocalDate.now().toString());
        }
        return analysisMapper.countAnalysisResult(param);
    }

    public List<AnalysisResult> getAnalysisResult(SearchParam param) {
        if (param.getBaseDate() == null || param.getBaseDate().isBlank()) {
            param.setBaseDate(LocalDate.now().toString());
        }
        return analysisMapper.selectAnalysisResult(param);
    }

    @Transactional
    public int insertAnalysisResult(SearchParam param) {
        analysisMapper.deleteAnalysisResultByBaseDate(param);
        return analysisMapper.insertAnalysisResult(param);
    }
}
