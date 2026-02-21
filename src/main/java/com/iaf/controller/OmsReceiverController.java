package com.iaf.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class OmsReceiverController {

    private static final Logger log = LoggerFactory.getLogger(OmsReceiverController.class);

    public OmsReceiverController() {
    }

    @PostMapping("/api/oms/alert")
    public ResponseEntity<Map<String, String>> receiveIafAlert(@RequestBody Map<String, Object> payload) {
        log.info("[OMS RECEIVER] 수신 - clientId: {}, baseDate: {}, alerts 건수: {}",
                payload.get("clientId"),
                payload.get("baseDate"),
                payload.get("alerts") instanceof java.util.List<?> list ? list.size() : 0);
        log.debug("[OMS RECEIVER] payload: {}", payload);

        return ResponseEntity.ok(Map.of("result", "OK"));
    }

}
