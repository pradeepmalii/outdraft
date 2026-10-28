package com.pradeepmali591.outDraft.service.impl;

import com.pradeepmali591.outDraft.dto.EmailRequest;
import com.pradeepmali591.outDraft.service.EmailGeneratorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.util.Map;



@Service

public class EmailGeneratorServiceImpl implements EmailGeneratorService {

    private final WebClient webClient;
    private final String apiKey;

    public EmailGeneratorServiceImpl(WebClient.Builder webClientBuilder,
                                     @Value("${gemini.api.url}") String baseUrl,
                                     @Value("${gemini.api.key}") String geminiApiKey){
        this.apiKey = geminiApiKey;
        this.webClient = webClientBuilder.baseUrl(baseUrl)
                .build();
    }


    @Override
    public String generateEmailReply(EmailRequest request) {

        //build prompt
        String prompt = buildPrompt(request);

        //Prepare raw json body

        ObjectMapper mapper = new ObjectMapper();

        String requestBody;
        try {
            requestBody = mapper.writeValueAsString(
                    Map.of(
                            "model", "gemini-2.5-flash",
                            "input", prompt
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Error creating request JSON", e);
        }


        //Send request
        String response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/interactions")
                .build())
        .header("x-goog-api-key", apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //Extract Response
        return extractresponseContent(response);

    }


    private String extractresponseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            JsonNode steps = root.path("steps");

            for (JsonNode step : steps) {
                if ("model_output".equals(step.path("type").asText())) {
                    for (JsonNode content : step.path("content")) {
                        if ("text".equals(content.path("type").asText())) {
                            return content.path("text").asText();
                        }
                    }
                }
            }

            throw new RuntimeException("No generated text found: " + response);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error parsing Gemini API response", e);
        }
    }


    private String buildPrompt(EmailRequest request){
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional email reply for the following email: ");
        if(request.getTone() != null && !request.getTone().isEmpty()){
            prompt.append("Use a ").append(request.getTone()).append(" tone.");
        }
        prompt.append("Original Email: \n").append(request.getEmailContent());

        return prompt.toString();
    }
}



//build prompt
//Prepare raw json body
//send request
//Extract Response
