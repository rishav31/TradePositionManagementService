package com.example.service;

import com.example.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PositionServiceTest {
    private TransactionService transactionService;
    private PositionService positionService;

    @BeforeEach
    void setUp() {
        transactionService = Mockito.mock(TransactionService.class);
        positionService = new PositionService(transactionService);
    }

    @Test
    void testGetPositions() {
        List<Position> mockPositions = Arrays.asList(
                new Position("REL", 100),
                new Position("ITC", -50)
        );
        when(transactionService.getPositions()).thenReturn(mockPositions);
        List<Position> result = positionService.getPositions();
        assertEquals(2, result.size());
        assertEquals("REL", result.get(0).getSecurityCode());
        assertEquals(100, result.get(0).getNetQuantity());
        assertEquals("ITC", result.get(1).getSecurityCode());
        assertEquals(-50, result.get(1).getNetQuantity());
    }

    @Test
    void testGetPositionsThrowsException() {
        when(transactionService.getPositions()).thenThrow(new RuntimeException("Service error"));
        Exception exception = assertThrows(RuntimeException.class, () -> positionService.getPositions());
        assertEquals("Service error", exception.getMessage());
    }
}
