package com.woopaca.noongil.infrastructure.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.noongil.infrastructure.config.AWSConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Slf4j
@Component
public class SqsMessageSender {

    private final SqsClient sqsClient;
    private final AWSConfiguration awsConfiguration;
    private final ObjectMapper objectMapper;

    public SqsMessageSender(SqsClient sqsClient, AWSConfiguration awsConfiguration, ObjectMapper objectMapper) {
        this.sqsClient = sqsClient;
        this.awsConfiguration = awsConfiguration;
        this.objectMapper = objectMapper;
    }

    public void send(SqsMessageBody body) {
        try {
            String messageBody = objectMapper.writeValueAsString(body);
            log.debug("messageBody: {}", messageBody);
            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(awsConfiguration.getSqsQueueUrl())
                    .messageBody(messageBody)
                    .build();
            SendMessageResponse sendMessageResponse = sqsClient.sendMessage(request);
            log.info("SQS 메시지 전송 완료. response: {}", sendMessageResponse);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("SQS 메시지 바디 JSON 변환 실패", e);
        }
    }
}
