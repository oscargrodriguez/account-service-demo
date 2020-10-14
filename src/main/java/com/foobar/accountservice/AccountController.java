package com.foobar.accountservice;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/accounts/")
@Tag(name = "accounts")
public class AccountController {

    AccountRepository accountRepository;
    AccountStreams accountStreams;
    AccountService accountService;

    public AccountController(AccountRepository accountRepository, AccountStreams accountStreams, AccountService accountService) {
        this.accountRepository = accountRepository;
        this.accountStreams = accountStreams;
        this.accountService = accountService;
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Account getAccountById(@PathVariable Integer id) {
        return accountRepository.findById(id).get();
    }

    @GetMapping(value = "userName/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Account getAccountByName(@PathVariable String username) {
        return accountRepository.findByUsername(username);
    }

    @PostMapping("signup")
    public Account createAccount(@RequestBody CreateAccount createAccount) {
        AccountEvent accountEvent = new AccountEvent(createAccount.getAccount().getAccountNumber());
        return accountRepository.save(createAccount.getAccount());
    }

    @PostMapping("{id}/activate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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


    @PostMapping("signin")
    public String login(@RequestParam String username, //
                        @RequestParam String password) {
        return accountService.signin(username, password);
    }


    @DeleteMapping(value = "{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable String username) {
        accountService.delete(username);
        return username;
    }


    @GetMapping(value = "me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public Account whoami(HttpServletRequest req) {
        return accountService.whoami(req);
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public String refresh(HttpServletRequest req) {
        return accountService.refresh(req.getRemoteUser());
    }
}
