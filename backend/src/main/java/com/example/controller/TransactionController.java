

package com.example.controller;

import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

import com.example.model.Transaction;
import com.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> processTransaction(@Valid @RequestBody Transaction transaction) {
        logger.info("POST /transactions called with: {}", transaction);
        transactionService.processTransaction(transaction);
        logger.info("Transaction processed successfully for tradeId={}, version={}", transaction.getTradeId(), transaction.getVersion());
        return ResponseEntity.ok().build();
    }


    @GetMapping("/latest-version")
    public int getLatestVersion(@RequestParam Long tradeId) {
        logger.info("GET /transactions/latest-version called with tradeId={}", tradeId);
        Integer v = transactionService.getLatestVersion(tradeId);
        logger.debug("Latest version for tradeId {}: {}", tradeId, v);
        return v == null ? 0 : v;
    }


    @GetMapping("/all")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }
}
