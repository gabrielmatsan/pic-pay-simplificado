package com.picpaysimplificado.dtos;

import java.math.BigDecimal;

public record TransactionRequestDTO(String receiverId, BigDecimal value) {

}
