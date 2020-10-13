package com.foobar.accountservice;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AccountController {

    AccountRepository accountRepository;
    AccountEventRepository accountEventRepository;

    public AccountController(AccountRepository accountRepository, AccountEventRepository accountEventRepository) {
        this.accountRepository = accountRepository;
        this.accountEventRepository = accountEventRepository;
    }

    @GetMapping("/accounts/{id}")
    public Account getAccount(@PathVariable Integer id) {
        return accountRepository.findById(id).get();
    }

    @PostMapping("/accounts")
    public Account createAccount(@RequestBody CreateAccount createAccount) {
        AccountEvent accountEvent = new AccountEvent(createAccount.getAccount().getAccountNumber());
        accountEventRepository.save(accountEvent);
        return accountRepository.save(createAccount.getAccount());
    }

    @PostMapping("/accounts/{id}/activate")
    public Account activateAccount(@PathVariable(value = "id") Integer id) {
        Account account = accountRepository.findById(id).get();
        AccountStatus accountStatus = account.getAccountStatus();
        if (AccountStatus.PENDING == accountStatus) {
            account.setAccountStatus(AccountStatus.ACTIVE);
            accountRepository.save(account);
            accountEventRepository.save(new AccountEvent(account.getAccountNumber(), AccountEventType.ACTIVE));
        } else {
            //throw exception
        }
        return account;
    }
}
