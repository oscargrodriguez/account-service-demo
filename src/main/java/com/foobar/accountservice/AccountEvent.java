package com.foobar.accountservice;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class AccountEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String accountNumber;
    private AccountEventType accountEventType = AccountEventType.PENDING;
    private Long timestamp = new Date().getTime();

    public AccountEvent() {
    }


    public AccountEvent(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountEvent(String accountNumber, AccountEventType accountEventType) {
        this.accountNumber = accountNumber;
        this.accountEventType = accountEventType;
    }

    public AccountEventType getAccountEventType() {
        return accountEventType;
    }

    public void setAccountEventType(AccountEventType accountEventType) {
        this.accountEventType = accountEventType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
