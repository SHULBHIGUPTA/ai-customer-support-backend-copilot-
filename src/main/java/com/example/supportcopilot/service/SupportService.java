package com.example.supportcopilot.service;

import com.example.supportcopilot.dto.SupportRequest;
import com.example.supportcopilot.model.SupportChat;
import com.example.supportcopilot.repository.SupportRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class SupportService {

    private final SupportRepository supportRepository;
    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.model}")
    private String geminiModel;

    public SupportService(SupportRepository supportRepository) {
        this.supportRepository = supportRepository;
        this.restTemplate = new RestTemplate();
    }

    public SupportChat askCopilot(SupportRequest request) {

        String prompt = buildPrompt(request);

        String aiResponse = callGemini(prompt);

        SupportChat chat = new SupportChat();
        chat.setCustomerName(request.getCustomerName());
        chat.setIssueCategory(request.getIssueCategory());
        chat.setCustomerMessage(request.getMessage());
        chat.setAiResponse(aiResponse);
        chat.setCreatedAt(LocalDateTime.now());
// chat.setAiResponse("Test AI Response");
        // return supportRepository.save(chat);
        return chat;
    }

    public List<SupportChat> getHistory() {
        // return supportRepository.findAll();
        return List.of();
    }

    public String healthCheck() {
        return "AI Customer Support Copilot Backend is running";
    }

    private String buildPrompt(SupportRequest request) {

        return """
                You are an AI Customer Support Copilot.

                Customer Name:
                %s

                Issue Category:
                %s

                Customer Message:
                %s

                Instructions:
                - Respond professionally and politely.
                - Give a clear solution.
                - Ask for additional information only if required.
                - Keep the answer easy to understand.
                - Do not mention that you are using Gemini.
                """.formatted(
                request.getCustomerName(),
                request.getIssueCategory(),
                request.getMessage()
        );
    }

    @SuppressWarnings("unchecked")
    private String callGemini(String prompt) {

        try {
            String url =
    "https://generativelanguage.googleapis.com/v1beta/models/"
        + geminiModel
        + ":generateContent?key="
        + geminiApiKey;

            Map<String, Object> part =
                    Map.of("text", prompt);

            Map<String, Object> content =
                    Map.of("parts", List.of(part));

            Map<String, Object> body =
                    Map.of("contents", List.of(content));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(body, headers);

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(url, entity, Map.class);

            if (response.getBody() == null) {
                return "AI response was empty.";
            }

            List<Map<String, Object>> candidates =
                    (List<Map<String, Object>>) response.getBody()
                            .get("candidates");

            if (candidates == null || candidates.isEmpty()) {
                return "No AI response generated.";
            }

            Map<String, Object> contentObj =
                    (Map<String, Object>) candidates.get(0)
                            .get("content");

            List<Map<String, Object>> parts =
                    (List<Map<String, Object>>) contentObj
                            .get("parts");

            if (parts == null || parts.isEmpty()) {
                return "AI response format was invalid.";
            }

            return parts.get(0).get("text").toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error while generating AI response: " + e.getMessage();
        }
    }
}