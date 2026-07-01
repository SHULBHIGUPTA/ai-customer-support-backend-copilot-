package com.example.supportcopilot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "support_chats")
public class SupportChat {

    @Id
    private String id;

    private String customerName;
    private String issueCategory;
    private String customerMessage;
    private String aiResponse;
    private LocalDateTime createdAt;
}