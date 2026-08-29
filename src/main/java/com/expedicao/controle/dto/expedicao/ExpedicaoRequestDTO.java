package com.expedicao.controle.dto.expedicao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ExpedicaoRequestDTO(

        @NotNull(message = "materialId e obrigatorio")
        Long materialId,

        @NotNull(message = "quantidadeExpedida e obrigatoria")
        @Positive(message = "quantidadeExpedida deve ser maior que zero")
        Integer quantidadeExpedida,

        @NotBlank(message = "destinatario e obrigatorio")
        String destinatario
) {}
