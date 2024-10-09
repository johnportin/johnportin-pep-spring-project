package com.example.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.InvalidMessageTextException;
import com.example.exception.MessagePostedWithoutAccountException;
import com.example.exception.MessageNotFoundException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository mr, AccountRepository ar) {
        messageRepository = mr;
        accountRepository = ar;
    }

    public Message postMessage(Message message) {
        if (message.getMessageText().equals("") || message.getMessageText().length() > 255) {
            throw new InvalidMessageTextException("Invalid Message Length.");
        }

        Optional<Account> a = accountRepository.findByAccountId(Integer.valueOf(message.getPostedBy()));
        a.orElseThrow(() -> new MessagePostedWithoutAccountException("Message must be posted with a legitimate account."));

        return messageRepository.save(message);
    }

    public ArrayList<Message> getAllMessages() {
        return new ArrayList<Message>(messageRepository.findAll());
    }

    public Message getMessageById(int messageId) {
        return messageRepository.findByMessageId(messageId).orElse(null);
    }
    
    @Transactional
    public boolean deleteMessageById(int messageId) {
        Optional<Message> m = messageRepository.findByMessageId(messageId);
        if (m.isPresent()) {
            messageRepository.deleteByMessageId(messageId);
            return true;
        } else {
            return false;
        }
    }

    public boolean patchMessageById(int messageId, String updatedText) {
        if (updatedText == null || updatedText.isBlank() || updatedText.equals("") || updatedText.length() > 255) {
            throw new InvalidMessageTextException("Invalid Message Length.");
        }

        Message message = messageRepository.findByMessageId(messageId).orElseThrow(() -> new MessageNotFoundException("No message with that message id."));
        message.setMessageText(updatedText);
        messageRepository.save(message);
        return true;
    }

    public ArrayList<Message> getMessagesByAccountId(int accountId) {
        return messageRepository.findAllByPostedBy(accountId).get();
        
    }
}
