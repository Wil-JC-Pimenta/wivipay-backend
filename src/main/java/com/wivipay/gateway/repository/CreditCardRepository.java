package com.wivipay.gateway.repository;

import com.wivipay.gateway.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, UUID>, 
                                           JpaSpecificationExecutor<CreditCard> {
    
    List<CreditCard> findByCustomerId(UUID customerId);
    
    Optional<CreditCard> findByProviderCardId(String providerCardId);
    
    Optional<CreditCard> findByCustomerIdAndIsDefaultTrue(UUID customerId);
    
    boolean existsByProviderCardId(String providerCardId);
}
