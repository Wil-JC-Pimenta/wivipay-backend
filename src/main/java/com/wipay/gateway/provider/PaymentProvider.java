package com.wipay.gateway.provider;

import com.wipay.gateway.dto.PaymentRequest;
import com.wipay.gateway.dto.PaymentResponse;
import java.math.BigDecimal;

public interface PaymentProvider {
    String getName();
    PaymentResponse authorize(PaymentRequest request);
    PaymentResponse capture(String transactionId);
    PaymentResponse refund(String transactionId, BigDecimal amount);
    boolean supports(String providerName);
} 