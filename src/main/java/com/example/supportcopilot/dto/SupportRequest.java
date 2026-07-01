package com.example.supportcopilot.dto;

import lombok.Data;

@Data
public class SupportRequest {

    private String customerName;
    private String issueCategory;
    private String message;
}