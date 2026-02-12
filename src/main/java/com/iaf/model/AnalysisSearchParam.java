package com.iaf.model;

public class AnalysisSearchParam {
    private Long clientId;
    private String category;
    private String baseDate;
    private String statusFilter;

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getBaseDate() { return baseDate; }
    public void setBaseDate(String baseDate) { this.baseDate = baseDate; }
    public String getStatusFilter() { return statusFilter; }
    public void setStatusFilter(String statusFilter) { this.statusFilter = statusFilter; }
}
