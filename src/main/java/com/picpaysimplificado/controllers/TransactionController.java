package com.picpaysimplificado.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.picpaysimplificado.annotations.AuthenticatedUser;
import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.dtos.TransactionRequestDTO;
import com.picpaysimplificado.dtos.user.UserResponseDTO;
import com.picpaysimplificado.services.TransactionService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

  @Autowired
  private TransactionService transactionService;

  @PostMapping("/")
  public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionRequestDTO body,
      @AuthenticatedUser User user) throws Exception {
    try {

      if (user == null) {
        throw new Exception("User not authenticated");
      }

      System.out.println(user);

      TransactionDTO newTransaction = new TransactionDTO(
          body.value(),
          user.getId(),
          body.receiverId());

      Transaction transaction = transactionService.createTransaction(newTransaction);
      return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    } catch (Exception e) {
      return ResponseEntity.status(400).body(null);
    }
  }
}
