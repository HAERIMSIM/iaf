package com.iaf.service;

import tools.jackson.databind.ObjectMapper;
import com.iaf.mapper.AnalysisMapper;
import com.iaf.mapper.OmsNotificationHistoryMapper;
import com.iaf.model.AnalysisResult;
import com.iaf.model.SearchParam;
import com.iaf.model.Client;
import com.iaf.model.OmsNotificationHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OmsNotificationService {

    private static final Logger log = LoggerFactory.getLogger(OmsNotificationService.class);
    private static final String BATCH_NAME = "IAF_OMS_NOTIFICATION";

    private final AnalysisMapper analysisMapper;
    private final OmsNotificationHistoryMapper omsNotificationHistoryMapper;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    public OmsNotificationService(AnalysisMapper analysisMapper,
                                  OmsNotificationHistoryMapper omsNotificationHistoryMapper, ObjectMapper objectMapper) {
        this.analysisMapper = analysisMapper;
        this.omsNotificationHistoryMapper = omsNotificationHistoryMapper;
        this.objectMapper = objectMapper;
        this.restClient = RestClient.create();
    }

    public int countOmsNotificationHistory(SearchParam param) {
        return omsNotificationHistoryMapper.countOmsNotificationHistory(param);
    }

    public List<OmsNotificationHistory> getOmsNotificationHistory(SearchParam param) {
        return omsNotificationHistoryMapper.selectOmsNotificationHistory(param);
    }

    public void sendNotification(String baseDate) {
        List<Client> clients = analysisMapper.selectClientList();

        for (Client client : clients) {

            Long clientId = client.getClientId();
            String clientName = client.getClientName();

            if (client.getOmsUrl() == null || client.getOmsUrl().isBlank()) {
                log.warn("[{}] 고객사 {}({}) - oms_url 미설정, 건너뜀", BATCH_NAME, clientName, clientId);
                saveOmsNotificationHistory(baseDate, clientId, null, null, "SKIP", "NO_URL", 0);
                continue;
            }

            long startTime = System.currentTimeMillis();
            try {
                SearchParam param = new SearchParam();
                param.setClientId(clientId);
                param.setBaseDate(baseDate);
                List<AnalysisResult> alerts = analysisMapper.selectAlertsByClientAndBaseDate(param);

                if (alerts.isEmpty()) {
                    log.info("[{}] 고객사 {}({}) - WARNING/DANGER 항목 없음, 발송 생략", BATCH_NAME, clientName, clientId);
                    continue;
                }

                Map<String, Object> payload = new HashMap<>();
                payload.put("baseDate", baseDate);
                payload.put("clientId", clientId);

                List<Map<String, Object>> alertList = alerts.stream().map(a -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("skuCode", a.getSkuCode());
                    item.put("skuName", a.getSkuName());
                    item.put("category", a.getCategory());
                    item.put("availableQty", a.getAvailableQty());
                    item.put("daysRemaining", a.getDaysRemaining());
                    item.put("status", a.getStatus());
                    item.put("recommendation", a.getRecommendation());
                    return item;
                }).toList();
                payload.put("alerts", alertList);

                String payloadJson = objectMapper.writeValueAsString(payload);

                restClient.post()
                        .uri(client.getOmsUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(payload)
                        .retrieve()
                        .toBodilessEntity();

                long elapsed = System.currentTimeMillis() - startTime;
                log.info("[{}] 고객사 {}({}) - 발송 성공, {}건, {}ms", BATCH_NAME, clientName, clientId, alerts.size(), elapsed);

                saveOmsNotificationHistory(baseDate, clientId, client.getOmsUrl(), payloadJson, "SUCCESS", null, elapsed);

            } catch (Exception e) {
                long elapsed = System.currentTimeMillis() - startTime;
                log.error("[{}] 고객사 {}({}) - 발송 실패, {}ms", BATCH_NAME, clientName, clientId, elapsed, e);

                saveOmsNotificationHistory(baseDate, clientId, client.getOmsUrl(), null, "FAIL", e.getMessage(), elapsed);
            }
        }
    }

    private void saveOmsNotificationHistory(String baseDate, Long clientId, String omsUrl,
                                    String requestPayload, String status, String errorMessage, long elapsedMs) {
        try {
            OmsNotificationHistory history = new OmsNotificationHistory();
            history.setBaseDate(baseDate);
            history.setClientId(clientId);
            history.setOmsUrl(omsUrl);
            history.setRequestPayload(requestPayload);
            history.setStatus(status);
            history.setErrorMessage(errorMessage);
            history.setElapsedMs(elapsedMs);
            omsNotificationHistoryMapper.insertOmsNotificationHistory(history);
        } catch (Exception e) {
            log.error("[{}] OMS 발송 이력 저장 실패 - clientId: {}", BATCH_NAME, clientId, e);
        }
    }
}
