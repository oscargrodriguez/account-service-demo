package com.foobar.accountservice;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Builder
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

    public AccountEvent(Integer id, String accountNumber, AccountEventType accountEventType, Long timestamp) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountEventType = accountEventType;
        this.timestamp = timestamp;
    }
}
