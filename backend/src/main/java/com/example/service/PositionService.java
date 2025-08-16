package com.example.service;

import com.example.model.Position;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PositionService {
    private final TransactionService transactionService;

    public PositionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public List<Position> getPositions() {
        return transactionService.getPositions();
    }
}
