package com.wipay.gateway.controller;

import com.wipay.gateway.dto.CreditCardRequest;
import com.wipay.gateway.dto.CreditCardResponse;
import com.wipay.gateway.service.CreditCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/credit-cards")
@RequiredArgsConstructor
@Tag(name = "Cartões de Crédito", description = "API de Gerenciamento de Cartões de Crédito")
public class CreditCardController {

    private final CreditCardService creditCardService;

    @Operation(summary = "Criar cartão de crédito", description = "Cria um novo cartão de crédito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cartão criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "409", description = "Cartão já existe"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_credit_cards:write')")
    public ResponseEntity<CreditCardResponse> createCreditCard(@Valid @RequestBody CreditCardRequest request) {
        CreditCardResponse creditCard = creditCardService.createCreditCard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creditCard);
    }

    @Operation(summary = "Atualizar cartão de crédito", description = "Atualiza um cartão de crédito existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cartão atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "404", description = "Cartão não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_credit_cards:write')")
    public ResponseEntity<CreditCardResponse> updateCreditCard(
            @PathVariable UUID id,
            @Valid @RequestBody CreditCardRequest request) {
        CreditCardResponse creditCard = creditCardService.updateCreditCard(id, request);
        return ResponseEntity.ok(creditCard);
    }

    @Operation(summary = "Consultar cartão de crédito", description = "Consulta um cartão de crédito por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cartão encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_credit_cards:read')")
    public ResponseEntity<CreditCardResponse> getCreditCard(@PathVariable UUID id) {
        CreditCardResponse creditCard = creditCardService.getCreditCard(id);
        return ResponseEntity.ok(creditCard);
    }

    @Operation(summary = "Listar cartões do cliente", description = "Lista todos os cartões de crédito de um cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cartões"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAuthority('SCOPE_credit_cards:read')")
    public ResponseEntity<List<CreditCardResponse>> getCustomerCreditCards(@PathVariable UUID customerId) {
        List<CreditCardResponse> creditCards = creditCardService.getCustomerCreditCards(customerId);
        return ResponseEntity.ok(creditCards);
    }

    @Operation(summary = "Consultar cartão padrão", description = "Consulta o cartão padrão de um cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cartão padrão encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "404", description = "Cartão padrão não encontrado")
    })
    @GetMapping("/customer/{customerId}/default")
    @PreAuthorize("hasAuthority('SCOPE_credit_cards:read')")
    public ResponseEntity<CreditCardResponse> getDefaultCreditCard(@PathVariable UUID customerId) {
        CreditCardResponse creditCard = creditCardService.getDefaultCreditCard(customerId);
        return ResponseEntity.ok(creditCard);
    }

    @Operation(summary = "Definir cartão padrão", description = "Define um cartão como padrão para um cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cartão definido como padrão"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
    })
    @PostMapping("/{id}/set-default")
    @PreAuthorize("hasAuthority('SCOPE_credit_cards:write')")
    public ResponseEntity<CreditCardResponse> setDefaultCreditCard(@PathVariable UUID id) {
        CreditCardResponse creditCard = creditCardService.setDefaultCreditCard(id);
        return ResponseEntity.ok(creditCard);
    }

    @Operation(summary = "Deletar cartão de crédito", description = "Deleta um cartão de crédito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cartão deletado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_credit_cards:write')")
    public ResponseEntity<Void> deleteCreditCard(@PathVariable UUID id) {
        creditCardService.deleteCreditCard(id);
        return ResponseEntity.noContent().build();
    }
}
