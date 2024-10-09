package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.InvalidCredentialsException;
import com.example.exception.InvalidUsernameOrPasswordException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    
    @Autowired
    public AccountService(AccountRepository ar) {
        accountRepository = ar;
    }
    public Account registerAccount(Account account) {
        System.out.println("Registering account...");

        if (account.getUsername().equals("") || account.getPassword().length() < 4) {
            throw new InvalidUsernameOrPasswordException("Username must be nonempty and password must be at least 4 characters.");
        }
        
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("Username already exists.");
        }
        Account newAccount = accountRepository.save(account);
        return newAccount;
    }
    public Account loginAccount(Account account) {
        return accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword())
            .orElseThrow(() -> new InvalidCredentialsException("Account not found. Try again or register a new account."));
    }
}
