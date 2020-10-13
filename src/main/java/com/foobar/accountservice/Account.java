package com.foobar.accountservice;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String accountNumber;

    private AccountStatus accountStatus = AccountStatus.PENDING;

    public Account() {
    }


    public Account(Integer id, String accountNumber, AccountStatus accountStatus) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountStatus = accountStatus;
    }
}

