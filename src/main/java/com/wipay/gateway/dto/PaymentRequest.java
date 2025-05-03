package com.wipay.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Requisição de pagamento")
public class PaymentRequest {
    
    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    @Schema(description = "Valor do pagamento", example = "100.00")
    private BigDecimal amount;
    
    @NotBlank(message = "A moeda é obrigatória")
    @Schema(description = "Moeda do pagamento", example = "BRL")
    private String currency;
    
    @NotBlank(message = "O método de pagamento é obrigatório")
    @Schema(description = "Método de pagamento (token do cartão, etc)", example = "tok_1Nq9Xr2eZvKYlo2C0QvJ2eZv")
    private String paymentMethod;
    
    @NotBlank(message = "O provedor é obrigatório")
    @Schema(description = "Provedor de pagamento", example = "cielo")
    private String provider;
    
    @Schema(description = "Descrição do pagamento", example = "Pagamento de teste")
    private String description;
    
    @Schema(description = "ID do cliente", example = "12345")
    private String customerId;
    
    @Schema(description = "Metadados adicionais", example = "{\"orderId\": \"123\"}")
    private String metadata;
} 