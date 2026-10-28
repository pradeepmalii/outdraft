package com.pradeepmali591.outDraft.controller;

import com.pradeepmali591.outDraft.dto.EmailRequest;
import com.pradeepmali591.outDraft.service.EmailGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailGeneratorController {

    private final EmailGeneratorService emailGeneratorService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest request){

        String response = emailGeneratorService.generateEmailReply(request);
        return ResponseEntity.ok(response);
    }

    


}
