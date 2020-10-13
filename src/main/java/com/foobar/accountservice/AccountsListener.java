package com.foobar.accountservice;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class AccountsListener {
    @StreamListener(AccountStreams.INPUT)
    public void handleAccounts(@Payload Account account) {
        System.out.println("Received accountEvent: {}" + account);
    }
}
