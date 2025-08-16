package com.example.service;

import com.example.model.Position;
import com.example.model.Transaction;
import com.example.model.TradeHistory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionService {
    private final Map<String, Integer> positions = new HashMap<>();
    private final Map<Long, Transaction> trades = new HashMap<>();
    private final TradeHistory tradeHistory = new TradeHistory();

    public synchronized void processTransaction(Transaction tx) {
        Long tradeId = tx.getTradeId();
        Integer version = tx.getVersion();
        String action = tx.getAction();
        String direction = tx.getDirection();
        String securityCode = tx.getSecurityCode();
        Integer quantity = tx.getQuantity();

        Integer latestVersion = tradeHistory.getLatestVersion(tradeId);
        if (latestVersion != null && version < latestVersion) {
            // Ignore out-of-order or old versions
            return;
        }

        if ("CANCEL".equalsIgnoreCase(action)) {
            // Remove the effect of the previous trade if exists
            Transaction prev = trades.get(tradeId);
            if (prev != null) {
                updatePosition(prev.getSecurityCode(), prev.getDirection(), -prev.getQuantity());
                trades.remove(tradeId);
            }
            tradeHistory.updateVersion(tradeId, version);
            return;
        }

        if ("INSERT".equalsIgnoreCase(action) || "UPDATE".equalsIgnoreCase(action)) {
            // Remove previous effect if updating
            Transaction prev = trades.get(tradeId);
            if (prev != null) {
                updatePosition(prev.getSecurityCode(), prev.getDirection(), -prev.getQuantity());
            }
            updatePosition(securityCode, direction, quantity);
            trades.put(tradeId, tx);
            tradeHistory.updateVersion(tradeId, version);
        }
    }

    private void updatePosition(String securityCode, String direction, int quantity) {
        int sign = "Buy".equalsIgnoreCase(direction) ? 1 : -1;
        positions.put(securityCode, positions.getOrDefault(securityCode, 0) + sign * quantity);
    }

    public List<Position> getPositions() {
        List<Position> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : positions.entrySet()) {
            result.add(new Position(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    public Integer getLatestVersion(Long tradeId) {
        return tradeHistory.getLatestVersion(tradeId);
    }
}
