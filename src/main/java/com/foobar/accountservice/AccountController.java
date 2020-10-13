package com.foobar.accountservice;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts/")
public class AccountController {

    AccountRepository accountRepository;
    AccountStreams accountStreams;

    public AccountController(AccountRepository accountRepository,
                             AccountStreams accountStreams) {
        this.accountRepository = accountRepository;
        this.accountStreams = accountStreams;
    }

    @GetMapping("{id}")
    public Account getAccount(@PathVariable Integer id) {
        return accountRepository.findById(id).get();
    }

    @PostMapping("")
    public Account createAccount(@RequestBody CreateAccount createAccount) {
        AccountEvent accountEvent = new AccountEvent(createAccount.getAccount().getAccountNumber());
        return accountRepository.save(createAccount.getAccount());
    }

    @PostMapping("{id}/activate")
    public Account activateAccount(@PathVariable(value = "id") Integer id) {
        Account account = accountRepository.findById(id).get();
        if (AccountStatus.PENDING == account.getAccountStatus()) {
            account.setAccountStatus(AccountStatus.ACTIVE);
            accountRepository.save(account);
            //Publish event
            MessageChannel messageChannel = accountStreams.outboundAccounts();
            messageChannel.send(MessageBuilder
                    .withPayload(account)
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                    .build());
        } else {
            //throw exception
        }
        return account;
    }
}
