package com.wivipay.gateway.service;

import com.wivipay.gateway.model.PaymentTransaction;
import com.wivipay.gateway.model.TransactionLog;
import com.wivipay.gateway.repository.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionLogService {
    
    private final TransactionLogRepository repository;
    
    @Transactional
    public TransactionLog logTransactionStatus(PaymentTransaction transaction, String status, String message) {
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setTransaction(transaction);
        transactionLog.setStatus(status);
        transactionLog.setMessage(message);
        
        TransactionLog savedLog = repository.save(transactionLog);
        log.info("Log de transação criado: {} - {} - {}", 
                transaction.getId(), status, message);
        
        return savedLog;
    }
    
    @Transactional(readOnly = true)
    public List<TransactionLog> getTransactionLogs(UUID transactionId) {
        return repository.findByTransactionIdOrderByCreatedAtDesc(transactionId);
    }
    
    @Transactional(readOnly = true)
    public List<TransactionLog> getTransactionLogsByStatus(UUID transactionId, String status) {
        return repository.findByTransactionIdAndStatus(transactionId, status);
    }
    
    @Transactional
    public void logPaymentAuthorization(PaymentTransaction transaction) {
        logTransactionStatus(transaction, "AUTHORIZED", 
                "Pagamento autorizado com sucesso");
    }
    
    @Transactional
    public void logPaymentCapture(PaymentTransaction transaction) {
        logTransactionStatus(transaction, "CAPTURED", 
                "Pagamento capturado com sucesso");
    }
    
    @Transactional
    public void logPaymentRefund(PaymentTransaction transaction, String amount) {
        logTransactionStatus(transaction, "REFUNDED", 
                "Pagamento estornado no valor de " + amount);
    }
    
    @Transactional
    public void logPaymentFailure(PaymentTransaction transaction, String errorMessage) {
        logTransactionStatus(transaction, "FAILED", 
                "Falha no pagamento: " + errorMessage);
    }
    
    @Transactional
    public void logPaymentPending(PaymentTransaction transaction) {
        logTransactionStatus(transaction, "PENDING", 
                "Pagamento em processamento");
    }
}
