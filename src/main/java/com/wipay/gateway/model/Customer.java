package com.wipay.gateway.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "customers")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotBlank(message = "ID externo é obrigatório")
    @Column(name = "external_id", nullable = false, unique = true)
    private String externalId;
    
    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false, length = 100)
    private String name;
    
    @Email(message = "Email deve ser válido")
    @Column(length = 100)
    private String email;
    
    @Pattern(regexp = "^\\d{11}|\\d{14}$", message = "CPF deve ter 11 dígitos ou CNPJ deve ter 14 dígitos")
    @Column(length = 20)
    private String document;
    
    @Pattern(regexp = "^\\+?\\d{10,14}$", message = "Telefone deve ser válido")
    @Column(length = 20)
    private String phone;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
