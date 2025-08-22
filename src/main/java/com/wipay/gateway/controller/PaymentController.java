package com.wipay.gateway.controller;

import com.wipay.gateway.dto.PaymentRequest;
import com.wipay.gateway.dto.PaymentResponse;
import com.wipay.gateway.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Pagamentos", description = "API de Pagamentos")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Autorizar pagamento", description = "Autoriza um pagamento através do provedor especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagamento autorizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/authorize")
    @PreAuthorize("hasAuthority('SCOPE_payments:write')")
    public ResponseEntity<PaymentResponse> authorize(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.authorize(request));
    }

    @Operation(summary = "Capturar pagamento", description = "Captura um pagamento previamente autorizado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagamento capturado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "404", description = "Transação não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/capture/{transactionId}")
    @PreAuthorize("hasAuthority('SCOPE_payments:write')")
    public ResponseEntity<PaymentResponse> capture(@PathVariable UUID transactionId) {
        return ResponseEntity.ok(paymentService.capture(transactionId));
    }

    @Operation(summary = "Estornar pagamento", description = "Estorna um pagamento previamente capturado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagamento estornado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "404", description = "Transação não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/refund/{transactionId}")
    @PreAuthorize("hasAuthority('SCOPE_payments:write')")
    public ResponseEntity<PaymentResponse> refund(
            @PathVariable UUID transactionId,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(paymentService.refund(transactionId, amount));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_payments:read')")
    @Operation(summary = "Consultar pagamento", description = "Consulta o status de um pagamento")
    @ApiResponse(responseCode = "200", description = "Pagamento encontrado")
    @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable UUID id) {
        PaymentResponse payment = paymentService.getPayment(id);
        return ResponseEntity.ok(payment);
    }
} 