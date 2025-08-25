package com.wivipay.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta de cliente")
public class CustomerResponse {
    
    @Schema(description = "ID do cliente", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
    
    @Schema(description = "ID externo do cliente", example = "CLI001")
    private String externalId;
    
    @Schema(description = "Nome completo do cliente", example = "João Silva")
    private String name;
    
    @Schema(description = "Email do cliente", example = "joao.silva@email.com")
    private String email;
    
    @Schema(description = "CPF ou CNPJ do cliente", example = "12345678901")
    private String document;
    
    @Schema(description = "Telefone do cliente", example = "+5511999999999")
    private String phone;
    
    @Schema(description = "Data de criação")
    private LocalDateTime createdAt;
    
    @Schema(description = "Data da última atualização")
    private LocalDateTime updatedAt;
}
