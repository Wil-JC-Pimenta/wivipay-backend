package com.wipay.gateway.repository;

import com.wipay.gateway.model.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, UUID>, 
                                               JpaSpecificationExecutor<TransactionLog> {
    
    List<TransactionLog> findByTransactionIdOrderByCreatedAtDesc(UUID transactionId);
    
    List<TransactionLog> findByTransactionIdAndStatus(UUID transactionId, String status);
}
