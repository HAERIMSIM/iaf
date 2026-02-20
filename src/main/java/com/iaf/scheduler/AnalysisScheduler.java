package com.iaf.scheduler;

import com.iaf.model.SearchParam;
import com.iaf.service.AnalysisService;
import com.iaf.service.BatchLogService;
import com.iaf.service.OmsNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AnalysisScheduler {

    private static final Logger log = LoggerFactory.getLogger(AnalysisScheduler.class);
    private static final String BATCH_NAME_ANALYSIS = "IAF_ANALYSIS_RESULT";
    private static final String BATCH_NAME_OMS = "IAF_OMS_NOTIFICATION";

    private final AnalysisService analysisService;
    private final BatchLogService batchLogService;
    private final OmsNotificationService omsNotificationService;

    public AnalysisScheduler(AnalysisService analysisService, BatchLogService batchLogService, OmsNotificationService omsNotificationService) {
        this.analysisService = analysisService;
        this.batchLogService = batchLogService;
        this.omsNotificationService = omsNotificationService;
    }

    @Scheduled(cron = "0 30 2 * * *", zone = "Asia/Seoul")
    public void runDailyAnalysis() {
        runAnalysis(LocalDate.now().toString());
    }

    public void runAnalysis(String baseDate) {
        SearchParam param = new SearchParam();
        param.setBaseDate(baseDate);

        log.info("[{} START] baseDate: {}", BATCH_NAME_ANALYSIS, baseDate);
        long startTime = System.currentTimeMillis();
        try {
            int insertCount = analysisService.insertAnalysisResult(param);
            long elapsed = System.currentTimeMillis() - startTime;

            String status = insertCount > 0 ? "SUCCESS" : "WARNING";

            if (insertCount == 0) {
                log.warn("[{} WARNING] baseDate: {}, 적재 건수 0건", BATCH_NAME_ANALYSIS, baseDate);
            }

            log.info("[{} END] baseDate: {}, 적재 건수: {}, 소요시간: {}ms", BATCH_NAME_ANALYSIS, baseDate, insertCount, elapsed);

            batchLogService.save(BATCH_NAME_ANALYSIS, baseDate, status, insertCount, elapsed, null);

        } catch (Exception e) {
            long elapsed = System.currentTimeMillis() - startTime;

            log.error("[{} ERROR] baseDate: {}, 소요시간: {}ms", BATCH_NAME_ANALYSIS, baseDate, elapsed, e);

            batchLogService.save(BATCH_NAME_ANALYSIS, baseDate, "FAIL", 0, elapsed, e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 5 * * *", zone = "Asia/Seoul")
    public void runDailyOmsNotification() {
        String baseDate = LocalDate.now().toString();

        if (!batchLogService.isCompleted(BATCH_NAME_ANALYSIS, baseDate)) {
            log.warn("[{}] baseDate: {} - ANALYSIS_RESULT 적재 미완료, OMS 발송 생략", BATCH_NAME_OMS, baseDate);
            batchLogService.save(BATCH_NAME_OMS, baseDate, "SKIP", 0, 0, "ANALYSIS_RESULT 적재 미완료");
            return;
        }

        log.info("[{} START] baseDate: {}", BATCH_NAME_OMS, baseDate);
        long startTime = System.currentTimeMillis();
        try {
            omsNotificationService.sendNotification(baseDate);
            long elapsed = System.currentTimeMillis() - startTime;
            log.info("[{} END] baseDate: {}, 소요시간: {}ms", BATCH_NAME_OMS, baseDate, elapsed);
            batchLogService.save(BATCH_NAME_OMS, baseDate, "SUCCESS", 0, elapsed, null);
        } catch (Exception e) {
            long elapsed = System.currentTimeMillis() - startTime;
            log.error("[{} ERROR] baseDate: {}, 소요시간: {}ms", BATCH_NAME_OMS, baseDate, elapsed, e);
            batchLogService.save(BATCH_NAME_OMS, baseDate, "FAIL", 0, elapsed, e.getMessage());
        }
    }
}
