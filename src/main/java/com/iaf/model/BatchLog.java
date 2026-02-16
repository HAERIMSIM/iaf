package com.iaf.model;

public class BatchLog {
    private Long batchLogId;
    private String batchName;
    private String baseDate;
    private String status;
    private Integer insertCount;
    private Long elapsedMs;
    private String errorMessage;
    private String createdAt;

    public Long getBatchLogId() { return batchLogId; }
    public void setBatchLogId(Long batchLogId) { this.batchLogId = batchLogId; }
    public String getBatchName() { return batchName; }
    public void setBatchName(String batchName) { this.batchName = batchName; }
    public String getBaseDate() { return baseDate; }
    public void setBaseDate(String baseDate) { this.baseDate = baseDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getInsertCount() { return insertCount; }
    public void setInsertCount(Integer insertCount) { this.insertCount = insertCount; }
    public Long getElapsedMs() { return elapsedMs; }
    public void setElapsedMs(Long elapsedMs) { this.elapsedMs = elapsedMs; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
