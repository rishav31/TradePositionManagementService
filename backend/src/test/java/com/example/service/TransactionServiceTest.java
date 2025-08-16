package com.example.service;

import com.example.model.Position;
import com.example.model.Transaction;

import com.example.repository.PositionRepository;
import com.example.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TransactionServiceTest {
    @Mock
    private PositionRepository positionRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    when(transactionRepository.findById(1L)).thenReturn(Optional.empty());
    when(positionRepository.findById("REL")).thenReturn(Optional.of(new Position("REL", 0)));
    when(positionRepository.save(any(Position.class))).thenAnswer(i -> i.getArguments()[0]);
    when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
    transactionService.processTransaction(tx);
    when(positionRepository.findAll()).thenReturn(List.of(new Position("REL", 100)));
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
    when(transactionRepository.findById(2L)).thenReturn(Optional.empty());
    when(positionRepository.findById("ITC")).thenReturn(Optional.of(new Position("ITC", 0)));
    when(positionRepository.save(any(Position.class))).thenAnswer(i -> i.getArguments()[0]);
    when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
    transactionService.processTransaction(tx);
    when(positionRepository.findAll()).thenReturn(List.of(new Position("ITC", -50)));
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
    when(transactionRepository.findById(3L)).thenReturn(Optional.empty());
    when(positionRepository.findById("INF")).thenReturn(Optional.of(new Position("INF", 0)));
    when(positionRepository.save(any(Position.class))).thenAnswer(i -> i.getArguments()[0]);
    when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
    transactionService.processTransaction(tx1);

    // Simulate update: previous transaction exists
    Transaction tx2 = new Transaction();
    tx2.setTradeId(3L);
    tx2.setVersion(2);
    tx2.setSecurityCode("INF");
    tx2.setQuantity(60);
    tx2.setAction("UPDATE");
    tx2.setDirection("Buy");
    when(transactionRepository.findById(3L)).thenReturn(Optional.of(tx1));
    transactionService.processTransaction(tx2);
    when(positionRepository.findAll()).thenReturn(List.of(new Position("INF", 60)));
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
    when(transactionRepository.findById(4L)).thenReturn(Optional.empty());
    when(positionRepository.findById("INF")).thenReturn(Optional.of(new Position("INF", 0)));
    when(positionRepository.save(any(Position.class))).thenAnswer(i -> i.getArguments()[0]);
    when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
    transactionService.processTransaction(tx1);

    // Simulate cancel: previous transaction exists
    Transaction tx2 = new Transaction();
    tx2.setTradeId(4L);
    tx2.setVersion(2);
    tx2.setSecurityCode("INF");
    tx2.setQuantity(30);
    tx2.setAction("CANCEL");
    tx2.setDirection("Buy");
    when(transactionRepository.findById(4L)).thenReturn(Optional.of(tx1));
    doNothing().when(transactionRepository).deleteById(4L);
    transactionService.processTransaction(tx2);
    when(positionRepository.findAll()).thenReturn(List.of(new Position("INF", 0)));
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
    when(transactionRepository.findById(5L)).thenReturn(Optional.empty());
    when(positionRepository.findById("INF")).thenReturn(Optional.of(new Position("INF", 0)));
    when(positionRepository.save(any(Position.class))).thenAnswer(i -> i.getArguments()[0]);
    when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
    transactionService.processTransaction(tx1);

    // Simulate out-of-order update: previous transaction exists
    Transaction tx2 = new Transaction();
    tx2.setTradeId(5L);
    tx2.setVersion(1);
    tx2.setSecurityCode("INF");
    tx2.setQuantity(60);
    tx2.setAction("UPDATE");
    tx2.setDirection("Buy");
    when(transactionRepository.findById(5L)).thenReturn(Optional.of(tx1));
    transactionService.processTransaction(tx2);
    when(positionRepository.findAll()).thenReturn(List.of(new Position("INF", 30)));
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
    when(transactionRepository.findById(999L)).thenReturn(Optional.empty());
    Exception exception = assertThrows(Exception.class, () -> transactionService.getLatestVersion(999L));
    assertNotNull(exception);
    }
}
