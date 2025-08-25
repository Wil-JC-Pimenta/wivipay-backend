package com.wivipay.gateway.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "credit_cards")
public class CreditCard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotNull(message = "Cliente é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @NotBlank(message = "ID do cartão no provedor é obrigatório")
    @Column(name = "provider_card_id", nullable = false, unique = true)
    private String providerCardId;
    
    @NotBlank(message = "Últimos 4 dígitos são obrigatórios")
    @Pattern(regexp = "^\\d{4}$", message = "Últimos 4 dígitos devem ter exatamente 4 números")
    @Column(name = "last_four_digits", nullable = false, length = 4)
    private String lastFourDigits;
    
    @NotBlank(message = "Bandeira é obrigatória")
    @Column(nullable = false, length = 20)
    private String brand;
    
    @NotNull(message = "Mês de expiração é obrigatório")
    @Min(value = 1, message = "Mês deve estar entre 1 e 12")
    @Max(value = 12, message = "Mês deve estar entre 1 e 12")
    @Column(name = "expiration_month", nullable = false)
    private Integer expirationMonth;
    
    @NotNull(message = "Ano de expiração é obrigatório")
    @Min(value = 2024, message = "Ano deve ser válido")
    @Column(name = "expiration_year", nullable = false)
    private Integer expirationYear;
    
    @Column(name = "is_default", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDefault = false;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
