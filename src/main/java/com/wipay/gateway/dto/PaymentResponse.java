package com.wipay.gateway.dto;

import com.wipay.gateway.model.PaymentTransaction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta de pagamento")
public class PaymentResponse {
    @Schema(description = "ID da transação", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
    
    @Schema(description = "Provedor de pagamento", example = "cielo")
    private String provider;
    
    @Schema(description = "ID da transação no provedor", example = "123456789")
    private String providerTransactionId;
    
    @Schema(description = "Valor do pagamento", example = "100.00")
    private BigDecimal amount;
    
    @Schema(description = "Moeda do pagamento", example = "BRL")
    private String currency;
    
    @Schema(description = "Status do pagamento", example = "AUTHORIZED")
    private PaymentTransaction.PaymentStatus status;
    
    @Schema(description = "Método de pagamento", example = "credit_card")
    private String paymentMethod;
    
    @Schema(description = "Mensagem de erro, se houver", example = "Cartão recusado")
    private String errorMessage;
    
    @Schema(description = "Data de criação da transação")
    private LocalDateTime createdAt;
    
    @Schema(description = "Data da última atualização")
    private LocalDateTime updatedAt;
} 