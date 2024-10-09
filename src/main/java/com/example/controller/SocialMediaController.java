package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

// import com.azul.crs.client.Response;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.InvalidCredentialsException;
import com.example.exception.InvalidMessageTextException;
import com.example.exception.InvalidUsernameOrPasswordException;
import com.example.exception.MessageNotFoundException;
import com.example.exception.MessagePostedWithoutAccountException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

// Define the SocialMediaController as a RestController (@Controller, @AutoConfigure?) 
@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;
    
    @Autowired
    public SocialMediaController(AccountService as, MessageService ms) {
        this.accountService = as;
        this.messageService = ms;
    }


    @PostMapping("register")
    public @ResponseBody ResponseEntity<Account> registerAccount(@RequestBody Account account) {
        Account registeredAccount = accountService.registerAccount(account);
        return ResponseEntity.status(HttpStatus.OK).body(registeredAccount);
    }

    @PostMapping("login")
    public @ResponseBody ResponseEntity<Account> loginAccount(@RequestBody Account account) {
        Account accountLoggedIn = accountService.loginAccount(account);
        return ResponseEntity.status(HttpStatus.OK).body(accountLoggedIn);
    }

    @PostMapping("messages")
    public @ResponseBody ResponseEntity<Message> postMessage(@RequestBody Message message) {
        Message postedMessage = messageService.postMessage(message);
        return ResponseEntity.status(HttpStatus.OK).body(postedMessage);
    }

    @GetMapping("messages")
    public @ResponseBody ResponseEntity<ArrayList<Message>> getAllMessages() {
        ArrayList<Message> messages = messageService.getAllMessages();
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    @GetMapping("messages/{messageId}")
    public @ResponseBody ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        Message message = messageService.getMessageById(messageId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @DeleteMapping("messages/{messageId}")
    public @ResponseBody ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId) {
        boolean rowsUpdated = messageService.deleteMessageById(messageId);
        return ResponseEntity.status(HttpStatus.OK).body(rowsUpdated ? 1 : null);
    }

    @PatchMapping("messages/{messageId}")
    public @ResponseBody ResponseEntity<Integer> patchMessageTextById(@PathVariable int messageId, @RequestBody Message messageText) {
        boolean rowsUpdated = messageService.patchMessageById(messageId, messageText.getMessageText());
        return ResponseEntity.status(HttpStatus.OK).body(rowsUpdated ? 1 : null);
    }

    @GetMapping("accounts/{accountId}/messages")
    public @ResponseBody ResponseEntity<ArrayList<Message>> getMessagesByAccountId(@PathVariable int accountId) {
        ArrayList<Message> messages = messageService.getMessagesByAccountId(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    @ExceptionHandler(InvalidUsernameOrPasswordException.class)
    public ResponseEntity<String> handleInvalidUsernameOrPasswordException(InvalidUsernameOrPasswordException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<String> handleDuplicateUsernameException(DuplicateUsernameException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(MessagePostedWithoutAccountException.class)
    public ResponseEntity<String> handleMessagePostedWithoutAccountException(MessagePostedWithoutAccountException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message posted by a user that does not exist.");
    }

    @ExceptionHandler(InvalidMessageTextException.class)
    public ResponseEntity<String> handleInvalidMessageTextException(InvalidMessageTextException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message must be between 0 and 255 characters.");
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<String> handleMessageNotFoundException(MessageNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

}
