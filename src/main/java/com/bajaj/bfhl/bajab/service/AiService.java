package com.bajaj.bfhl.bajab.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;

import java.util.*;
@Service
public class AiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String ask(String question) {

        String url =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key="
                        + apiKey;


        Map<String, Object> body = new HashMap<>();
        body.put("contents", List.of(
                Map.of("parts", List.of(
                        Map.of("text", question)
                ))
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> req =
                new HttpEntity<>(body, headers);

        Map response =
                restTemplate.postForObject(url, req, Map.class);

        String answer = extractText(response);
        return answer.split("\\s+")[0];
    }

    private String extractText(Map response) {

        if (response == null)
            throw new RuntimeException("Empty AI response");

        List<?> candidates = (List<?>) response.get("candidates");
        if (candidates == null || candidates.isEmpty())
            throw new RuntimeException("No candidates");

        Map<?, ?> candidate = (Map<?, ?>) candidates.get(0);
        Map<?, ?> content = (Map<?, ?>) candidate.get("content");

        List<?> parts = (List<?>) content.get("parts");
        Map<?, ?> part = (Map<?, ?>) parts.get(0);

        Object text = part.get("text");
        if (text == null)
            throw new RuntimeException("No text in AI response");

        return text.toString();
    }
}
