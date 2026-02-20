package com.iaf.mapper;

import com.iaf.model.SearchParam;
import com.iaf.model.OmsNotificationHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OmsNotificationHistoryMapper {
    void insertOmsNotificationHistory(OmsNotificationHistory history);
    int countOmsNotificationHistory(SearchParam param);
    List<OmsNotificationHistory> selectOmsNotificationHistory(SearchParam param);
}
