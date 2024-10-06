package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    
    @Autowired
    public AccountService(AccountRepository ar) {
        accountRepository = ar;
    }
    public Account registerAccount(Account account) {
        System.out.println("Successfully registered ACcount....");
        account.setAccountId(1);
        return account;
    }
}
