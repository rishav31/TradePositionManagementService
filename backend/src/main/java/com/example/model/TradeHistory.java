package com.example.model;

import java.util.HashMap;
import java.util.Map;

public class TradeHistory {
    private Map<Long, Integer> latestVersionPerTradeId = new HashMap<>();

    public Integer getLatestVersion(Long tradeId) {
        return latestVersionPerTradeId.get(tradeId);
    }

    public void updateVersion(Long tradeId, Integer version) {
        latestVersionPerTradeId.put(tradeId, version);
    }
}
