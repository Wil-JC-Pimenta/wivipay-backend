package com.wivipay.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Requisição de cartão de crédito")
public class CreditCardRequest {
    
    @NotNull(message = "ID do cliente é obrigatório")
    @Schema(description = "ID do cliente", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID customerId;
    
    @NotBlank(message = "ID do cartão no provedor é obrigatório")
    @Schema(description = "ID do cartão no provedor", example = "card_123456789")
    private String providerCardId;
    
    @NotBlank(message = "Últimos 4 dígitos são obrigatórios")
    @Pattern(regexp = "^\\d{4}$", message = "Últimos 4 dígitos devem ter exatamente 4 números")
    @Schema(description = "Últimos 4 dígitos do cartão", example = "1234")
    private String lastFourDigits;
    
    @NotBlank(message = "Bandeira é obrigatória")
    @Schema(description = "Bandeira do cartão", example = "VISA")
    private String brand;
    
    @NotNull(message = "Mês de expiração é obrigatório")
    @Min(value = 1, message = "Mês deve estar entre 1 e 12")
    @Max(value = 12, message = "Mês deve estar entre 1 e 12")
    @Schema(description = "Mês de expiração", example = "12")
    private Integer expirationMonth;
    
    @NotNull(message = "Ano de expiração é obrigatório")
    @Min(value = 2024, message = "Ano deve ser válido")
    @Schema(description = "Ano de expiração", example = "2025")
    private Integer expirationYear;
    
    @Schema(description = "Se é o cartão padrão", example = "false")
    private Boolean isDefault = false;
}
