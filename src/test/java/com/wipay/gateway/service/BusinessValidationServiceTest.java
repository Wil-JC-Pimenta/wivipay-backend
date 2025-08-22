package com.wipay.gateway.service;

import com.wipay.gateway.dto.PaymentRequest;
import com.wipay.gateway.model.CreditCard;
import com.wipay.gateway.repository.CustomerRepository;
import com.wipay.gateway.repository.CreditCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessValidationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private BusinessValidationService businessValidationService;

    private PaymentRequest paymentRequest;
    private CreditCard creditCard;

    @BeforeEach
    void setUp() {
        paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(BigDecimal.valueOf(100.00));
        paymentRequest.setCurrency("BRL");
        paymentRequest.setPaymentMethod("tok_123456789");
        paymentRequest.setProvider("stripe");
        paymentRequest.setCustomerId("CLI001");

        creditCard = new CreditCard();
        creditCard.setLastFourDigits("1234");
        creditCard.setBrand("VISA");
        creditCard.setExpirationMonth(12);
        creditCard.setExpirationYear(LocalDate.now().getYear() + 1);
    }

    @Test
    void shouldValidatePaymentRequestSuccessfully() {
        when(customerRepository.existsByExternalId("CLI001")).thenReturn(true);

        assertDoesNotThrow(() -> businessValidationService.validatePaymentRequest(paymentRequest));
    }

    @Test
    void shouldValidateCustomerSuccessfully() {
        when(customerRepository.existsByExternalId("CLI001")).thenReturn(true);

        assertDoesNotThrow(() -> businessValidationService.validateCustomer("CLI001"));
    }

    @Test
    void shouldValidateCreditCardSuccessfully() {
        assertDoesNotThrow(() -> businessValidationService.validateCreditCard(creditCard));
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNull() {
        paymentRequest.setAmount(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validatePaymentRequest(paymentRequest));

        assertEquals("Valor é obrigatório", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsZero() {
        paymentRequest.setAmount(BigDecimal.ZERO);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validatePaymentRequest(paymentRequest));

        assertEquals("Valor deve ser maior que 0.01", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {
        paymentRequest.setAmount(BigDecimal.valueOf(-10.00));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validatePaymentRequest(paymentRequest));

        assertEquals("Valor deve ser maior que 0.01", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCurrencyIsNull() {
        paymentRequest.setCurrency(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validatePaymentRequest(paymentRequest));

        assertEquals("Moeda é obrigatória", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCurrencyIsEmpty() {
        paymentRequest.setCurrency("");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validatePaymentRequest(paymentRequest));

        assertEquals("Moeda é obrigatória", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCurrencyIsNotSupported() {
        paymentRequest.setCurrency("JPY");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validatePaymentRequest(paymentRequest));

        assertEquals("Moeda não suportada: JPY. Moedas suportadas: [BRL, USD, EUR, GBP]", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenProviderIsNull() {
        paymentRequest.setProvider(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validatePaymentRequest(paymentRequest));

        assertEquals("Provedor é obrigatório", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenProviderIsNotSupported() {
        paymentRequest.setProvider("mercadopago");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validatePaymentRequest(paymentRequest));

        assertEquals("Provedor não suportado: mercadopago. Provedores suportados: [stripe, cielo, paypal]", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenProviderAndCurrencyIncompatible() {
        paymentRequest.setProvider("cielo");
        paymentRequest.setCurrency("USD");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validatePaymentRequest(paymentRequest));

        assertEquals("Moeda USD não é suportada pelo provedor cielo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAmountExceedsMaximum() {
        paymentRequest.setAmount(BigDecimal.valueOf(1000000.00));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validatePaymentRequest(paymentRequest));

        assertEquals("Valor não pode exceder 999999.99 para moeda BRL", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAmountHasTooManyDecimals() {
        paymentRequest.setAmount(BigDecimal.valueOf(100.123));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validatePaymentRequest(paymentRequest));

        assertEquals("Valor não pode ter mais de 2 casas decimais", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPaymentMethodIsNull() {
        paymentRequest.setPaymentMethod(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validatePaymentRequest(paymentRequest));

        assertEquals("Método de pagamento é obrigatório", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPaymentMethodIsEmpty() {
        paymentRequest.setPaymentMethod("");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validatePaymentRequest(paymentRequest));

        assertEquals("Método de pagamento é obrigatório", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenStripeTokenIsInvalid() {
        paymentRequest.setProvider("stripe");
        paymentRequest.setPaymentMethod("invalid_token");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validatePaymentRequest(paymentRequest));

        assertEquals("Token do Stripe deve começar com 'tok_' ou 'card_'", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCieloTokenIsInvalid() {
        paymentRequest.setProvider("cielo");
        paymentRequest.setPaymentMethod("invalid_token");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validatePaymentRequest(paymentRequest));

        assertEquals("Token da Cielo deve começar com 'card_'", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPayPalTokenIsInvalid() {
        paymentRequest.setProvider("paypal");
        paymentRequest.setPaymentMethod("invalid_token");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validatePaymentRequest(paymentRequest));

        assertEquals("Token do PayPal deve começar com 'paypal_'", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        when(customerRepository.existsByExternalId("CLI001")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validatePaymentRequest(paymentRequest));

        assertEquals("Cliente não encontrado: CLI001", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCardExpirationMonthInvalid() {
        creditCard.setExpirationMonth(0);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validateCreditCard(creditCard));

        assertEquals("Mês de expiração deve estar entre 1 e 12", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCardExpirationMonthTooHigh() {
        creditCard.setExpirationMonth(13);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validateCreditCard(creditCard));

        assertEquals("Mês de expiração deve estar entre 1 e 12", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCardExpirationYearInvalid() {
        creditCard.setExpirationYear(LocalDate.now().getYear() + 100); // Ano muito distante no futuro

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validateCreditCard(creditCard));

        assertEquals("Ano de expiração deve ser válido", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCardExpired() {
        creditCard.setExpirationMonth(LocalDate.now().getMonthValue());
        creditCard.setExpirationYear(LocalDate.now().getYear() - 1);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validateCreditCard(creditCard));

        assertEquals("Cartão expirado", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCardBrandNotSupported() {
        creditCard.setBrand("DISCOVER");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validateCreditCard(creditCard));

        assertEquals("Bandeira não suportada: DISCOVER. Bandeiras suportadas: [VISA, MASTERCARD, AMEX, ELO, HIPERCARD]", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLastFourDigitsInvalid() {
        creditCard.setLastFourDigits("123");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validateCreditCard(creditCard));

        assertEquals("Últimos 4 dígitos devem ter exatamente 4 números", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLastFourDigitsNotNumeric() {
        creditCard.setLastFourDigits("12ab");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> businessValidationService.validateCreditCard(creditCard));

        assertEquals("Últimos 4 dígitos devem ter exatamente 4 números", exception.getMessage());
    }

    @Test
    void shouldValidateValidStripeToken() {
        paymentRequest.setProvider("stripe");
        paymentRequest.setPaymentMethod("tok_123456789");
        when(customerRepository.existsByExternalId("CLI001")).thenReturn(true);

        assertDoesNotThrow(() -> businessValidationService.validatePaymentRequest(paymentRequest));
    }

    @Test
    void shouldValidateValidCieloToken() {
        paymentRequest.setProvider("cielo");
        paymentRequest.setPaymentMethod("card_123456789");
        paymentRequest.setCurrency("BRL");
        when(customerRepository.existsByExternalId("CLI001")).thenReturn(true);

        assertDoesNotThrow(() -> businessValidationService.validatePaymentRequest(paymentRequest));
    }

    @Test
    void shouldValidateValidPayPalToken() {
        paymentRequest.setProvider("paypal");
        paymentRequest.setPaymentMethod("paypal_123456789");
        when(customerRepository.existsByExternalId("CLI001")).thenReturn(true);

        assertDoesNotThrow(() -> businessValidationService.validatePaymentRequest(paymentRequest));
    }
}
