package com.iaf.mapper;

import com.iaf.model.AnalysisResult;
import com.iaf.model.SearchParam;
import com.iaf.model.Client;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnalysisMapper {
    List<Client> selectClientList();
    List<String> selectCategoryListByClientId(Long clientId);
    List<AnalysisResult> selectAlertsByClientAndBaseDate(SearchParam param);
    int countAnalysisResult(SearchParam param);
    List<AnalysisResult> selectAnalysisResult(SearchParam param);
    void deleteAnalysisResultByBaseDate(SearchParam param);
    int insertAnalysisResult(SearchParam param);
}
