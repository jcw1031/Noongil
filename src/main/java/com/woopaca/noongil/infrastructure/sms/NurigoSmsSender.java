package com.woopaca.noongil.infrastructure.sms;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NurigoSmsSender implements SmsSender {

    private final SmsConfiguration smsConfiguration;
    private final DefaultMessageService messageService;

    public NurigoSmsSender(SmsConfiguration smsConfiguration, DefaultMessageService messageService) {
        this.smsConfiguration = smsConfiguration;
        this.messageService = messageService;
    }

    @Override
    public void send(String phoneNumber, String messageText) {
        Message message = createMessage(phoneNumber, messageText);
        SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);
        messageService.sendOne(request);
        log.info("SMS 전송 - phoneNumber: {}", phoneNumber);
    }

    private Message createMessage(String phoneNumber, String messageText) {
        Message message = new Message();
        message.setFrom(smsConfiguration.getSenderNumber());
        message.setTo(phoneNumber);
        message.setText(messageText);
        return message;
    }
}
