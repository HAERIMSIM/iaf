<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>업체별 입·출고 분석</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/analysis.css">
</head>
<body>

<jsp:include page="../common/header.jsp"/>
<jsp:include page="../common/menu.jsp"/>
<h2> 업체별 입·출고 분석 </h2>

<form method="get" action="${pageContext.request.contextPath}/analysis" class="search-form">
    <div class="form-group">
        <label for="clientId">업체(고객사)</label>
        <select id="clientId" name="clientId">
            <option value="">전체</option>
        </select>
    </div>
    <div class="form-row">
        <div class="form-item">
            <label for="baseDate">기준일</label>
            <input type="date" id="baseDate" name="baseDate">
        </div>
        <div class="form-item">
            <label for="recentDays">분석 기준</label>
            <select id="recentDays" name="recentDays">
                <option value="30">최근 7일</option>
                <option value="60">최근 14일</option>
                <option value="90">최근 28일</option>
            </select>
        </div>
        <div class="form-item">
            <label for="statusFilter">상태 필터</label>
            <select id="statusFilter" name="statusFilter">
                <option value="">전체</option>
                <option value="SAFE">✅안전</option>
                <option value="WARNING">⚠️주의</option>
                <option value="DANGER">🚨위험</option>
            </select>
        </div>
        <div class="form-item form-item-btn">
            <button type="submit">조회</button>
        </div>
    </div>
</form>

<table>
    <colgroup>
        <col style="width: 12%;">
        <col style="width: 12%;">
        <col style="width: 12%;">
        <col span="9" style="width: 7.1%;">
    </colgroup>
    <thead>
        <tr>
            <th colspan="7">기준 데이터</th>
            <th colspan="3" class="analysis-result">분석</th>
            <th colspan="2" class="analysis-oms">조치</th>
        </tr>
        <tr>
            <th>카테고리</th>
            <th>SKU 코드</th>
            <th>SKU 명</th>
            <th>입고수량</th>
            <th>출고수량</th>
            <th>기준 재고</th>
            <th>일평균 출고</th>
            <th class="analysis-result">예상 소진일</th>
            <th class="analysis-result">상태</th>
            <th class="analysis-result">권고사항</th>
            <th class="analysis-oms">OMS 전송</th>
            <th class="analysis-oms">전송 결과</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td colspan="12" class="no-data">데이터가 없습니다.</td>
        </tr>
   </tbody>
</table>

<script>
    if (!document.getElementById('baseDate').value) {
        document.getElementById('baseDate').value = new Date().toISOString().substring(0, 10);
    }
</script>
</body>
</html>