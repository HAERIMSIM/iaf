package com.iaf.mapper;

import com.iaf.model.SearchParam;
import com.iaf.model.OmsNotification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmsNotificationMapper {
    Map<String, Object> selectStatusSummary(SearchParam param);
    void insertOmsNotification(OmsNotification history);
    int countOmsNotification(SearchParam param);
    List<OmsNotification> selectOmsNotification(SearchParam param);
}
