package com.github.kasiaOsowska.homeassistant.library.service;

import com.github.kasiaOsowska.homeassistant.library.dto.BookDto;
import com.github.kasiaOsowska.homeassistant.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenAIService {

    private static final Logger logger = LoggerFactory.getLogger(OpenAIService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpHeaders headers;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public String getBookRecommendation(String description, List<BookDto> books) {
        HttpEntity<String> request = new HttpEntity<>(createRequestBody(description, books), headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_API_URL, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return parseResponse(response.getBody());
            } else {
                logger.error("Failed to get response from OpenAI: " + response.getStatusCode());
                logger.error("Response body: " + response.getBody());
                throw new RuntimeException("Failed to get response from OpenAI");
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 429) {
                logger.error("Rate limit exceeded: " + e.getMessage());
                throw new RuntimeException("Rate limit exceeded. Please try again later.");
            } else if (e.getStatusCode().value() == 403) {
                logger.error("Insufficient quota: " + e.getMessage());
                throw new RuntimeException("Insufficient quota. Please check your plan and billing details.");
            } else if (e.getStatusCode().value() == 401) {
                logger.error("Unauthorized: " + e.getMessage());
                throw new RuntimeException("Unauthorized. Please check your API key.");
            } else {
                logger.error("HTTP error occurred: " + e.getMessage());
                throw new RuntimeException("An error occurred while communicating with OpenAI.");
            }
        }
    }

    private String createRequestBody(String description, List<BookDto> books) {
        String booksList = books.stream()
                .map(book -> "Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Genre: " + book.getGenre())
                .collect(Collectors.joining("\\n"));

        return "{"
                + "\"model\": \"gpt-3.5-turbo\","
                + "\"messages\": [{\"role\": \"user\", \"content\": \"To jest lista książek z mojej bazy danych: \\n" + booksList + "\\n\\n i opis użytkownika co chce przeczytać: " + escapeJson(description) + ", zaproponuj jedną lub kilka książek, napisz jedynie tytuł i autora\"}],"
                + "\"max_tokens\": 200"
                + "}";
    }

    private String escapeJson(String text) {
        return text.replace("\"", "\\\"").replace("\n", "\\n");
    }

    private String parseResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode messageNode = rootNode.path("choices").get(0).path("message").path("content");
            return messageNode.asText();
        } catch (IOException e) {
            logger.error("Failed to parse response from OpenAI: " + e.getMessage());
            throw new RuntimeException("Failed to parse response from OpenAI", e);
        }
    }
}
