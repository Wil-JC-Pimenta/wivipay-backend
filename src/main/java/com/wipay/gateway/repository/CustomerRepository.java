package com.wipay.gateway.repository;

import com.wipay.gateway.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID>, 
                                        JpaSpecificationExecutor<Customer> {
    
    Optional<Customer> findByExternalId(String externalId);
    
    boolean existsByExternalId(String externalId);
    
    boolean existsByEmail(String email);
    
    boolean existsByDocument(String document);
}
