package com.example.supportcopilot.controller;

import com.example.supportcopilot.dto.SupportRequest;
import com.example.supportcopilot.model.SupportChat;
import com.example.supportcopilot.service.SupportService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
@CrossOrigin(origins = "*")
public class SupportController {

    private final SupportService supportService;

    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok(supportService.healthCheck());
    }

    @PostMapping("/ask")
    public ResponseEntity<SupportChat> askCopilot(
            @RequestBody SupportRequest request) {

        SupportChat response = supportService.askCopilot(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<SupportChat>> getHistory() {
        return ResponseEntity.ok(supportService.getHistory());
    }
}