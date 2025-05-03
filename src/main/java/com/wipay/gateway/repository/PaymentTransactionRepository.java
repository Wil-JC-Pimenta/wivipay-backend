package com.wipay.gateway.repository;

import com.wipay.gateway.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, UUID>, 
                                                    JpaSpecificationExecutor<PaymentTransaction> {
    
    PaymentTransaction findByProviderTransactionId(String providerTransactionId);
} 