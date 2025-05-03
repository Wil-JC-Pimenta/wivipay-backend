package com.wipay.gateway.service;

import com.wipay.gateway.dto.PaymentRequest;
import com.wipay.gateway.dto.PaymentResponse;
import com.wipay.gateway.model.PaymentTransaction;
import com.wipay.gateway.provider.PaymentProvider;
import com.wipay.gateway.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final List<PaymentProvider> providers;
    private final PaymentTransactionRepository repository;

    @Transactional
    public PaymentResponse authorize(PaymentRequest request) {
        PaymentProvider provider = findProvider(request.getProvider());
        
        PaymentResponse response = provider.authorize(request);
        saveTransaction(response);
        
        return response;
    }

    @Transactional
    public PaymentResponse capture(UUID transactionId) {
        PaymentTransaction transaction = repository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));
        
        PaymentProvider provider = findProvider(transaction.getProvider());
        PaymentResponse response = provider.capture(transaction.getProviderTransactionId());
        
        updateTransaction(transaction, response);
        return response;
    }

    @Transactional
    public PaymentResponse refund(UUID transactionId, BigDecimal amount) {
        PaymentTransaction transaction = repository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));
        
        PaymentProvider provider = findProvider(transaction.getProvider());
        PaymentResponse response = provider.refund(transaction.getProviderTransactionId(), amount);
        
        updateTransaction(transaction, response);
        return response;
    }

    private PaymentProvider findProvider(String providerName) {
        return providers.stream()
                .filter(p -> p.supports(providerName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Provedor não suportado: " + providerName));
    }

    private void saveTransaction(PaymentResponse response) {
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setProvider(response.getProvider());
        transaction.setProviderTransactionId(response.getProviderTransactionId());
        transaction.setAmount(response.getAmount());
        transaction.setCurrency(response.getCurrency());
        transaction.setStatus(response.getStatus());
        transaction.setPaymentMethod(response.getPaymentMethod());
        transaction.setRawResponse(response.toString());
        
        repository.save(transaction);
    }

    private void updateTransaction(PaymentTransaction transaction, PaymentResponse response) {
        transaction.setStatus(response.getStatus());
        transaction.setRawResponse(response.toString());
        repository.save(transaction);
    }
} 