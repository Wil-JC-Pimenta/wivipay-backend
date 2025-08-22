package com.wipay.gateway.controller;

import com.wipay.gateway.model.TransactionLog;
import com.wipay.gateway.service.TransactionLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transaction-logs")
@RequiredArgsConstructor
@Tag(name = "Logs de Transações", description = "API de Consulta de Logs de Transações")
public class TransactionLogController {

    private final TransactionLogService transactionLogService;

    @Operation(summary = "Consultar logs de transação", description = "Consulta todos os logs de uma transação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logs encontrados"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @GetMapping("/transaction/{transactionId}")
    @PreAuthorize("hasAuthority('SCOPE_transactions:read')")
    public ResponseEntity<List<TransactionLog>> getTransactionLogs(@PathVariable UUID transactionId) {
        List<TransactionLog> logs = transactionLogService.getTransactionLogs(transactionId);
        return ResponseEntity.ok(logs);
    }

    @Operation(summary = "Consultar logs por status", description = "Consulta logs de uma transação por status específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logs encontrados"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @GetMapping("/transaction/{transactionId}/status/{status}")
    @PreAuthorize("hasAuthority('SCOPE_transactions:read')")
    public ResponseEntity<List<TransactionLog>> getTransactionLogsByStatus(
            @PathVariable UUID transactionId,
            @PathVariable String status) {
        List<TransactionLog> logs = transactionLogService.getTransactionLogsByStatus(transactionId, status);
        return ResponseEntity.ok(logs);
    }
}
