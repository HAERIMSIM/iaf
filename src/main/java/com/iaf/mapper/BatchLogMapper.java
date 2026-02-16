package com.iaf.mapper;

import com.iaf.model.BatchLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BatchLogMapper {
    void insertBatchLog(BatchLog batchLog);
}
