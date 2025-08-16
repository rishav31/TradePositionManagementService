package com.example.controller;

import com.example.model.Position;
import com.example.service.PositionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PositionControllerTest {
    private MockMvc mockMvc;
    @Mock
    private PositionService positionService;
    @InjectMocks
    private PositionController positionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(positionController).build();
    }

    @Test
    void testGetPositions() throws Exception {
        when(positionService.getPositions()).thenReturn(Arrays.asList(
                new Position("REL", 100),
                new Position("ITC", -50)
        ));
        mockMvc.perform(get("/positions").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].securityCode").value("REL"))
                .andExpect(jsonPath("$[0].netQuantity").value(100))
                .andExpect(jsonPath("$[1].securityCode").value("ITC"))
                .andExpect(jsonPath("$[1].netQuantity").value(-50));
    }

    @Test
    void testGetPositionsServiceThrowsException() throws Exception {
        when(positionService.getPositions()).thenThrow(new RuntimeException("Service error"));
        org.springframework.web.util.NestedServletException thrown = org.junit.jupiter.api.Assertions.assertThrows(
            org.springframework.web.util.NestedServletException.class,
            () -> mockMvc.perform(get("/positions").accept(MediaType.APPLICATION_JSON))
        );
        org.junit.jupiter.api.Assertions.assertTrue(thrown.getCause() instanceof RuntimeException);
    }
}
