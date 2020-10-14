package com.foobar.accountservice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get an account by its id")
    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the account",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content)})
    public Account getAccountById(@Parameter(description = "account id to be searched") @PathVariable Integer id) {
        return accountRepository.findById(id).get();
    }

    @Operation(summary = "Get an account by its username")
    @GetMapping(value = "userName/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Account getAccountByName(@PathVariable String username) {
        return accountRepository.findByUsername(username);
    }

    @Operation(summary = "Create account")
    @PostMapping("signup")
    public Account createAccount(@RequestParam String accountNumber,
                                 @RequestParam String userName,
                                 @RequestParam String password) {
        Account account = new Account(accountNumber, userName, password, Role.ROLE_CLIENT);
        return accountRepository.save(account);
    }

    @Operation(summary = "Activate account")
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


    @Operation(summary = "Log in and get jwt token")
    @PostMapping("signin")
    public String login(@RequestParam String username, //
                        @RequestParam String password) {
        return accountService.signin(username, password);
    }


    @Operation(summary = "Delete account by its username")
    @DeleteMapping(value = "{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable String username) {
        accountService.delete(username);
        return username;
    }


    @Operation(summary = "Get logged account data")
    @GetMapping(value = "me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public Account whoami(HttpServletRequest req) {
        return accountService.whoami(req);
    }

    @Operation(summary = "Obtain new jwt token")
    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public String refresh(HttpServletRequest req) {
        return accountService.refresh(req.getRemoteUser());
    }
}
