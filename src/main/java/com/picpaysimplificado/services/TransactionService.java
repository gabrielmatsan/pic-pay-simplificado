package com.picpaysimplificado.services;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.repositories.TransactionRepository;
import com.picpaysimplificado.repositories.UserRepository;

@Service
public class TransactionService {

  @Autowired
  private UserService userService;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private NotificationService notificationService;

  @Autowired
  private UserRepository userRepository;

  public Transaction createTransaction(TransactionDTO transaction) throws Exception {

    User isReceiverExists = this.userService.findUserById(transaction.receiverId());

    if (isReceiverExists == null) {
      throw new Exception("Receiver not found with id: " + transaction.receiverId());
    }

    if (transaction.value().compareTo(BigDecimal.ZERO) <= 0) {
      throw new Exception("Transaction value must be greater than zero");
    }

    User sender = this.userService.findUserById(transaction.senderId());

    User receiver = this.userService.findUserById(transaction.receiverId());

    userService.validateTransaction(sender, transaction.value());

    boolean isAuthorized = this.isAuthorizedTransaction(sender, transaction.value());
    if (!isAuthorized) {
      throw new Exception("Transaction not authorized");
    }

    Transaction newTransaction = new Transaction();
    newTransaction.setAmount(transaction.value());
    newTransaction.setSender(sender);
    newTransaction.setReceiver(receiver);
    newTransaction.setTimestamp(java.time.LocalDateTime.now());

    sender.setBalance(sender.getBalance().subtract(transaction.value()));
    receiver.setBalance(receiver.getBalance().add(transaction.value()));

    this.transactionRepository.save(newTransaction);
    this.userRepository.save(sender);
    this.userRepository.save(receiver);

    this.notificationService.sendNotification(sender, "Transaction sent");
    this.notificationService.sendNotification(receiver, "Transaction received");

    return newTransaction;
  }

  public boolean isAuthorizedTransaction(User sender, BigDecimal value) {
    ResponseEntity<Map> authorizedResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize",
        Map.class);

    if (authorizedResponse.getStatusCode().is2xxSuccessful() &&
        authorizedResponse.getBody() != null) {

      String message = (String) authorizedResponse.getBody().get("message");

      return "Autorizado".equalsIgnoreCase(message);
    }

    return false;
  }
}
