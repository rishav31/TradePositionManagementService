package com.example.controller;

import com.example.model.Transaction;
import com.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public void processTransaction(@RequestBody Transaction transaction) {
        transactionService.processTransaction(transaction);
    }
}
