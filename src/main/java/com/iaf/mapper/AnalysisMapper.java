package com.iaf.mapper;

import com.iaf.model.AnalysisResult;
import com.iaf.model.SearchParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AnalysisMapper {
    Map<String, Object> selectStatusSummary(SearchParam param);
    int selectAlertClientCount(SearchParam param);
    int selectOmsSuccessCount(SearchParam param);
    List<AnalysisResult> selectAlertsByClientAndBaseDate(SearchParam param);
    int countAnalysisResult(SearchParam param);
    List<AnalysisResult> selectAnalysisResult(SearchParam param);
    void deleteAnalysisResultByBaseDate(SearchParam param);
    int insertAnalysisResult(SearchParam param);
}
