package com.woopaca.noongil.infrastructure.sms;

public interface SmsSender {

    void send(String phoneNumber, String messageText);
}
