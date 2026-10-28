package com.pradeepmali591.outDraft.service;

import com.pradeepmali591.outDraft.dto.EmailRequest;

public interface EmailGeneratorService {


    String generateEmailReply(EmailRequest request);
}
