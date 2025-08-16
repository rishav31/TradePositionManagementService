package com.example.controller;

import com.example.model.Position;
import com.example.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/positions")
@CrossOrigin(origins = "*")
public class PositionController {
    private static final Logger logger = LoggerFactory.getLogger(PositionController.class);
    @Autowired
    private PositionService positionService;

    @GetMapping
    public List<Position> getPositions() {
    logger.info("GET /positions called");
    List<Position> positions = positionService.getPositions();
    logger.debug("Positions returned: {}", positions);
    return positions;
    }
}
