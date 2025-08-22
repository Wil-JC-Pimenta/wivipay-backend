package com.wipay.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.wipay.gateway.model.Customer;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Requisição de cliente")
public class CustomerRequest {
    
    @NotBlank(message = "ID externo é obrigatório")
    @Schema(description = "ID externo do cliente", example = "CLI001")
    private String externalId;
    
    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome completo do cliente", example = "João Silva")
    private String name;
    
    @Email(message = "Email deve ser válido")
    @Schema(description = "Email do cliente", example = "joao.silva@email.com")
    private String email;
    
    @Pattern(regexp = "^\\d{11}|\\d{14}$", message = "CPF deve ter 11 dígitos ou CNPJ deve ter 14 dígitos")
    @Schema(description = "CPF ou CNPJ do cliente", example = "12345678901")
    private String document;
    
    @Pattern(regexp = "^\\+?\\d{10,14}$", message = "Telefone deve ser válido")
    @Schema(description = "Telefone do cliente", example = "+5511999999999")
    private String phone;
    
    public Customer toCustomer() {
        Customer customer = new Customer();
        customer.setExternalId(this.externalId);
        customer.setName(this.name);
        customer.setEmail(this.email);
        customer.setDocument(this.document);
        customer.setPhone(this.phone);
        return customer;
    }
}
