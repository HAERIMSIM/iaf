package com.iaf.scheduler;

import com.iaf.mapper.BatchLogMapper;
import com.iaf.model.AnalysisSearchParam;
import com.iaf.model.BatchLog;
import com.iaf.service.AnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AnalysisScheduler {

    private static final Logger log = LoggerFactory.getLogger(AnalysisScheduler.class);
    private static final String BATCH_NAME = "IAF_ANALYSIS_RESULT";

    private final AnalysisService analysisService;
    private final BatchLogMapper batchLogMapper;

    public AnalysisScheduler(AnalysisService analysisService, BatchLogMapper batchLogMapper) {
        this.analysisService = analysisService;
        this.batchLogMapper = batchLogMapper;
    }

    @Scheduled(cron = "0 30 2 * * *", zone = "Asia/Seoul")
    public void runDailyAnalysis() {

        String baseDate = LocalDate.now().toString();
        AnalysisSearchParam param = new AnalysisSearchParam();
        param.setBaseDate(baseDate);

        log.info("[{} BATCH START] - baseDate: {}", BATCH_NAME, baseDate);
        long startTime = System.currentTimeMillis();
        try {
            int insertCount = analysisService.insertAnalysisResult(param);
            long elapsed = System.currentTimeMillis() - startTime;

            String status = insertCount > 0 ? "SUCCESS" : "WARNING";

            if (insertCount == 0) {
                log.warn("[{} BATCH WARNING] - baseDate: {}, 적재 건수 0건", BATCH_NAME, baseDate);
            }

            log.info("[{} BATCH {}] - baseDate: {}, 적재 건수: {}, 소요시간: {}ms", BATCH_NAME, status, baseDate, insertCount, elapsed);

            BatchLog batchLog = new BatchLog();
            batchLog.setBatchName(BATCH_NAME);
            batchLog.setBaseDate(baseDate);
            batchLog.setStatus(status);
            batchLog.setInsertCount(insertCount);
            batchLog.setElapsedMs(elapsed);
            batchLogMapper.insertBatchLog(batchLog);

        } catch (Exception e) {
            long elapsed = System.currentTimeMillis() - startTime;

            log.error("[{} BATCH ERROR] - baseDate: {}, 소요시간: {}ms", BATCH_NAME, baseDate, elapsed, e);

            try {
                BatchLog batchLog = new BatchLog();
                batchLog.setBatchName(BATCH_NAME);
                batchLog.setBaseDate(baseDate);
                batchLog.setStatus("FAIL");
                batchLog.setInsertCount(0);
                batchLog.setElapsedMs(elapsed);
                batchLog.setErrorMessage(e.getMessage());
                batchLogMapper.insertBatchLog(batchLog);
            } catch (Exception logEx) {
                log.error("[{} BATCH LOG INSERT ERROR]", BATCH_NAME, logEx);
            }
        }
    }
}
