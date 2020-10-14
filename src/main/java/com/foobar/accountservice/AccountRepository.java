package com.foobar.accountservice;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByUsername(String username);

    boolean existsByUsername(String userName);

    @Transactional
    void deleteByUsername(String username);
}
