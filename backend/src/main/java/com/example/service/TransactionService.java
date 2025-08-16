package com.example.service;

import com.example.exception.ResourceNotFoundException;

import com.example.model.Position;
import com.example.model.Transaction;
import com.example.repository.PositionRepository;
import com.example.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service

public class TransactionService {
    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    public synchronized void processTransaction(Transaction tx) {
        if (tx.getTradeId() == null || tx.getVersion() == null || tx.getSecurityCode() == null ||
            tx.getQuantity() == null || tx.getAction() == null || tx.getDirection() == null) {
            throw new IllegalArgumentException("Invalid transaction data");
        }
        Long tradeId = tx.getTradeId();
        Integer version = tx.getVersion();
        String action = tx.getAction();
        String direction = tx.getDirection();
        String securityCode = tx.getSecurityCode();
        Integer quantity = tx.getQuantity();

        // Find latest transaction for this tradeId
        Optional<Transaction> prevOpt = transactionRepository.findById(tradeId);
        Transaction prev = prevOpt.orElse(null);

        if ("CANCEL".equalsIgnoreCase(action)) {
            if (prev != null) {
                updatePosition(prev.getSecurityCode(), prev.getDirection(), -prev.getQuantity());
                transactionRepository.deleteById(tradeId);
            }
            return;
        }

        if ("INSERT".equalsIgnoreCase(action) || "UPDATE".equalsIgnoreCase(action)) {
            if (prev != null) {
                updatePosition(prev.getSecurityCode(), prev.getDirection(), -prev.getQuantity());
            }
            updatePosition(securityCode, direction, quantity);
            transactionRepository.save(tx);
        }
    }


    private void updatePosition(String securityCode, String direction, int quantity) {
        int sign = "Buy".equalsIgnoreCase(direction) ? 1 : -1;
        Position position = positionRepository.findById(securityCode).orElse(new Position(securityCode, 0));
        position.setNetQuantity(position.getNetQuantity() + sign * quantity);
        positionRepository.save(position);
    }


    public List<Position> getPositions() {
        return positionRepository.findAll();
    }


    public Integer getLatestVersion(Long tradeId) {
        Transaction tx = transactionRepository.findById(tradeId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
        return tx.getVersion();
    }
}
