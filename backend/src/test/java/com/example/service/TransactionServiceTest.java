package com.example.service;

import com.example.model.Position;
import com.example.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService();
    }

    @Test
    void testInsertBuyTransaction() {
        Transaction tx = new Transaction();
        tx.setTradeId(1L);
        tx.setVersion(1);
        tx.setSecurityCode("REL");
        tx.setQuantity(100);
        tx.setAction("INSERT");
        tx.setDirection("Buy");
        transactionService.processTransaction(tx);
        List<Position> positions = transactionService.getPositions();
        assertEquals(1, positions.size());
        assertEquals(100, positions.get(0).getNetQuantity());
    }

    @Test
    void testInsertSellTransaction() {
        Transaction tx = new Transaction();
        tx.setTradeId(2L);
        tx.setVersion(1);
        tx.setSecurityCode("ITC");
        tx.setQuantity(50);
        tx.setAction("INSERT");
        tx.setDirection("Sell");
        transactionService.processTransaction(tx);
        List<Position> positions = transactionService.getPositions();
        assertEquals(1, positions.size());
        assertEquals(-50, positions.get(0).getNetQuantity());
    }

    @Test
    void testUpdateTransaction() {
        Transaction tx1 = new Transaction();
        tx1.setTradeId(3L);
        tx1.setVersion(1);
        tx1.setSecurityCode("INF");
        tx1.setQuantity(30);
        tx1.setAction("INSERT");
        tx1.setDirection("Buy");
        transactionService.processTransaction(tx1);

        Transaction tx2 = new Transaction();
        tx2.setTradeId(3L);
        tx2.setVersion(2);
        tx2.setSecurityCode("INF");
        tx2.setQuantity(60);
        tx2.setAction("UPDATE");
        tx2.setDirection("Buy");
        transactionService.processTransaction(tx2);

        List<Position> positions = transactionService.getPositions();
        assertEquals(1, positions.size());
        assertEquals(60, positions.get(0).getNetQuantity());
    }

    @Test
    void testCancelTransaction() {
        Transaction tx1 = new Transaction();
        tx1.setTradeId(4L);
        tx1.setVersion(1);
        tx1.setSecurityCode("INF");
        tx1.setQuantity(30);
        tx1.setAction("INSERT");
        tx1.setDirection("Buy");
        transactionService.processTransaction(tx1);

        Transaction tx2 = new Transaction();
        tx2.setTradeId(4L);
        tx2.setVersion(2);
        tx2.setSecurityCode("INF");
        tx2.setQuantity(30);
        tx2.setAction("CANCEL");
        tx2.setDirection("Buy");
        transactionService.processTransaction(tx2);

        List<Position> positions = transactionService.getPositions();
        assertEquals(1, positions.size());
        assertEquals(0, positions.get(0).getNetQuantity());
    }

    @Test
    void testOutOfOrderTransactionIgnored() {
        Transaction tx1 = new Transaction();
        tx1.setTradeId(5L);
        tx1.setVersion(2);
        tx1.setSecurityCode("INF");
        tx1.setQuantity(30);
        tx1.setAction("INSERT");
        tx1.setDirection("Buy");
        transactionService.processTransaction(tx1);

        Transaction tx2 = new Transaction();
        tx2.setTradeId(5L);
        tx2.setVersion(1);
        tx2.setSecurityCode("INF");
        tx2.setQuantity(60);
        tx2.setAction("UPDATE");
        tx2.setDirection("Buy");
        transactionService.processTransaction(tx2);

        List<Position> positions = transactionService.getPositions();
        assertEquals(1, positions.size());
        assertEquals(30, positions.get(0).getNetQuantity());
    }

    @Test
    void testProcessTransactionWithInvalidData() {
        Transaction tx = new Transaction();
        // Missing required fields
        Exception exception = assertThrows(Exception.class, () -> transactionService.processTransaction(tx));
        assertNotNull(exception);
    }

    @Test
    void testGetLatestVersionNotFound() {
        Exception exception = assertThrows(Exception.class, () -> transactionService.getLatestVersion(999L));
        assertNotNull(exception);
    }
}
