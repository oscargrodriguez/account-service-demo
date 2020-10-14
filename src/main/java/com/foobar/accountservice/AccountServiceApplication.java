package com.foobar.accountservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountServiceApplication implements CommandLineRunner {

    @Autowired
    private AccountService accountService;

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }


    @Override
    public void run(String... params) throws Exception {
        Account admin = new Account();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRole(Role.ROLE_ADMIN);
        admin.setAccountNumber("1");

        accountService.signup(admin);
    }

}
