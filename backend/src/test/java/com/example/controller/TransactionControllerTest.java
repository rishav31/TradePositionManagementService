package com.example.controller;

import com.example.exception.ResourceNotFoundException;
import com.example.model.Transaction;
import com.example.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TransactionControllerTest {
    private MockMvc mockMvc;
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void testProcessTransaction() throws Exception {
        Transaction tx = new Transaction();
        tx.setTradeId(1L);
        tx.setVersion(1);
        tx.setSecurityCode("REL");
        tx.setQuantity(100);
        tx.setAction("INSERT");
        tx.setDirection("Buy");
        String json = "{" +
                "\"tradeId\":1," +
                "\"version\":1," +
                "\"securityCode\":\"REL\"," +
                "\"quantity\":100," +
                "\"action\":\"INSERT\"," +
                "\"direction\":\"Buy\"}";
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
        verify(transactionService, times(1)).processTransaction(any(Transaction.class));
    }

    @Test
    void testProcessTransactionValidationError() throws Exception {
    // Missing required field 'securityCode'
    String json = "{" +
        "\"tradeId\":1," +
        "\"version\":1," +
        "\"quantity\":100," +
        "\"action\":\"INSERT\"," +
        "\"direction\":\"Buy\"}";
    mockMvc.perform(post("/transactions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isBadRequest());
    }

    @Test
    void testProcessTransactionThrowsResourceNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Not found")).when(transactionService).processTransaction(any(Transaction.class));
        String json = "{" +
                "\"tradeId\":1," +
                "\"version\":1," +
                "\"securityCode\":\"REL\"," +
                "\"quantity\":100," +
                "\"action\":\"INSERT\"," +
                "\"direction\":\"Buy\"}";
        org.springframework.web.util.NestedServletException thrown = org.junit.jupiter.api.Assertions.assertThrows(
            org.springframework.web.util.NestedServletException.class,
            () -> mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        );
        Assertions.assertTrue(thrown.getCause() instanceof ResourceNotFoundException);
    }

    @Test
    void testGetLatestVersion() throws Exception {
        when(transactionService.getLatestVersion(1L)).thenReturn(2);
        mockMvc.perform(get("/transactions/latest-version?tradeId=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    void testGetLatestVersionNotFound() throws Exception {
        when(transactionService.getLatestVersion(99L)).thenThrow(new ResourceNotFoundException("Not found"));
        org.springframework.web.util.NestedServletException thrown = org.junit.jupiter.api.Assertions.assertThrows(
            org.springframework.web.util.NestedServletException.class,
            () -> mockMvc.perform(get("/transactions/latest-version?tradeId=99"))
        );
        Assertions.assertTrue(thrown.getCause() instanceof ResourceNotFoundException);
    }
}
