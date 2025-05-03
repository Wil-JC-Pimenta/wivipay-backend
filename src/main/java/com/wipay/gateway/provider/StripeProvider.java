package com.wipay.gateway.provider;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import com.wipay.gateway.dto.PaymentRequest;
import com.wipay.gateway.dto.PaymentResponse;
import com.wipay.gateway.model.PaymentTransaction;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class StripeProvider implements PaymentProvider {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public String getName() {
        return "stripe";
    }

    @Override
    public PaymentResponse authorize(PaymentRequest request) {
        try {
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", request.getAmount().multiply(BigDecimal.valueOf(100)).longValue());
            chargeParams.put("currency", request.getCurrency().toLowerCase());
            chargeParams.put("source", request.getPaymentMethod());
            chargeParams.put("description", request.getDescription());
            chargeParams.put("capture", false);

            Charge charge = Charge.create(chargeParams);

            PaymentResponse response = new PaymentResponse();
            response.setProvider("stripe");
            response.setProviderTransactionId(charge.getId());
            response.setAmount(request.getAmount());
            response.setCurrency(request.getCurrency());
            response.setStatus(PaymentTransaction.PaymentStatus.AUTHORIZED);
            response.setPaymentMethod(request.getPaymentMethod());

            return response;
        } catch (StripeException e) {
            log.error("Erro ao autorizar pagamento no Stripe", e);
            throw new RuntimeException("Erro ao processar pagamento: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponse capture(String transactionId) {
        try {
            Charge charge = Charge.retrieve(transactionId);
            Charge capturedCharge = charge.capture();

            PaymentResponse response = new PaymentResponse();
            response.setProvider("stripe");
            response.setProviderTransactionId(capturedCharge.getId());
            response.setAmount(BigDecimal.valueOf(capturedCharge.getAmount()).divide(BigDecimal.valueOf(100)));
            response.setCurrency(capturedCharge.getCurrency().toUpperCase());
            response.setStatus(PaymentTransaction.PaymentStatus.CAPTURED);
            response.setPaymentMethod(capturedCharge.getSource().getId());

            return response;
        } catch (StripeException e) {
            log.error("Erro ao capturar pagamento no Stripe", e);
            throw new RuntimeException("Erro ao capturar pagamento: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponse refund(String transactionId, BigDecimal amount) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("charge", transactionId);
            if (amount != null) {
                params.put("amount", amount.multiply(BigDecimal.valueOf(100)).longValue());
            }

            Refund refund = Refund.create(params);

            PaymentResponse response = new PaymentResponse();
            response.setProvider("stripe");
            response.setProviderTransactionId(refund.getId());
            response.setAmount(BigDecimal.valueOf(refund.getAmount()).divide(BigDecimal.valueOf(100)));
            response.setCurrency(refund.getCurrency().toUpperCase());
            response.setStatus(PaymentTransaction.PaymentStatus.REFUNDED);

            return response;
        } catch (StripeException e) {
            log.error("Erro ao estornar pagamento no Stripe", e);
            throw new RuntimeException("Erro ao estornar pagamento: " + e.getMessage());
        }
    }

    @Override
    public boolean supports(String providerName) {
        return "stripe".equalsIgnoreCase(providerName);
    }
} 