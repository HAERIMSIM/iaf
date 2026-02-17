package com.iaf.mapper;

import com.iaf.model.OmsNotificationHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OmsNotificationHistoryMapper {
    void insertOmsNotificationHistory(OmsNotificationHistory history);
}
