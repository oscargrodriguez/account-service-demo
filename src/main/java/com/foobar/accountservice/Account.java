package com.foobar.accountservice;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String accountNumber;
    private AccountStatus accountStatus = AccountStatus.PENDING;
    private String username;
    private String password;
    private Role role;

    public Account() {
    }
}

