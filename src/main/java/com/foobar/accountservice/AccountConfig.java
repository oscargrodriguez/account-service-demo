package com.foobar.accountservice;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(AccountStreams.class)
public class AccountConfig {
}
