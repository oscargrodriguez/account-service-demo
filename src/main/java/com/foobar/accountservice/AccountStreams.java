package com.foobar.accountservice;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface AccountStreams {
    String INPUT = "accounts-in";
    String OUTPUT = "accounts-out";

    @Input(INPUT)
    SubscribableChannel inboundAccounts();

    @Output(OUTPUT)
    MessageChannel outboundAccounts();
}
