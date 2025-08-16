
package com.example.controller;

import org.springframework.http.ResponseEntity;
import javax.validation.Valid;

import com.example.model.Transaction;
import com.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> processTransaction(@Valid @RequestBody Transaction transaction) {
        transactionService.processTransaction(transaction);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/latest-version")
    public int getLatestVersion(@RequestParam Long tradeId) {
        Integer v = transactionService.getLatestVersion(tradeId);
        return v == null ? 0 : v;
    }
}
