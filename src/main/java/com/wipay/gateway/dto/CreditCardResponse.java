package com.wipay.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta de cartão de crédito")
public class CreditCardResponse {
    
    @Schema(description = "ID do cartão", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
    
    @Schema(description = "ID do cliente", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID customerId;
    
    @Schema(description = "ID do cartão no provedor", example = "card_123456789")
    private String providerCardId;
    
    @Schema(description = "Últimos 4 dígitos do cartão", example = "1234")
    private String lastFourDigits;
    
    @Schema(description = "Bandeira do cartão", example = "VISA")
    private String brand;
    
    @Schema(description = "Mês de expiração", example = "12")
    private Integer expirationMonth;
    
    @Schema(description = "Ano de expiração", example = "2025")
    private Integer expirationYear;
    
    @Schema(description = "Se é o cartão padrão", example = "false")
    private Boolean isDefault;
    
    @Schema(description = "Data de criação")
    private LocalDateTime createdAt;
    
    @Schema(description = "Data da última atualização")
    private LocalDateTime updatedAt;
}
