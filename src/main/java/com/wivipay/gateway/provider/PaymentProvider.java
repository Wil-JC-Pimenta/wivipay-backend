package com.wivipay.gateway.provider;

import com.wivipay.gateway.dto.PaymentRequest;
import com.wivipay.gateway.dto.PaymentResponse;
import java.math.BigDecimal;

public interface PaymentProvider {
    String getName();
    PaymentResponse authorize(PaymentRequest request);
    PaymentResponse capture(String transactionId);
    PaymentResponse refund(String transactionId, BigDecimal amount);
    boolean supports(String providerName);
} 