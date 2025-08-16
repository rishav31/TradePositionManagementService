package com.example.controller;

import com.example.model.Position;
import com.example.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/positions")
@CrossOrigin(origins = "*")
public class PositionController {
    @Autowired
    private PositionService positionService;

    @GetMapping
    public List<Position> getPositions() {
        return positionService.getPositions();
    }
}
