package com.foobar.accountservice;

import com.foobar.accountservice.exception.CustomException;
import com.foobar.accountservice.repository.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String signin(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, accountRepository.findByUsername(username).getRole());
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String signup(Account account) {
        if (!accountRepository.existsByUsername(account.getUsername())) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            accountRepository.save(account);
            return jwtTokenProvider.createToken(account.getUsername(), account.getRole());
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public void delete(String username) {
        accountRepository.deleteByUsername(username);
    }

    public Account search(String username) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
        }
        return account;
    }

    public Account whoami(HttpServletRequest req) {
        return accountRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }

    public String refresh(String username) {
        return jwtTokenProvider.createToken(username, accountRepository.findByUsername(username).getRole());
    }

}
