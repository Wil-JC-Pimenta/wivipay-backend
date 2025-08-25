package com.wivipay.gateway.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "payment_transactions")
public class PaymentTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String provider;
    
    @Column(nullable = false)
    private String providerTransactionId;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private String currency;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    
    @Column(nullable = false)
    private String paymentMethod;
    
    @Column(columnDefinition = "TEXT")
    private String rawResponse;
    
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(length = 255)
    private String description;
    
    @Column(name = "customer_id", length = 100)
    private String customerId;
    
    @Column(columnDefinition = "TEXT")
    private String metadata;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public enum PaymentStatus {
        PENDING,
        AUTHORIZED,
        CAPTURED,
        REFUNDED,
        FAILED
    }
} 