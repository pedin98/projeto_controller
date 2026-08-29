package com.expedicao.controle.dto.material;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record MaterialRequestDTO(

        @NotBlank(message = "nome e obrigatorio")
        String nome,

        @NotBlank(message = "sku e obrigatorio")
        @Size(max = 50, message = "sku deve ter no maximo 50 caracteres")
        String sku,

        @NotNull(message = "quantidadeEstoque e obrigatoria")
        @PositiveOrZero(message = "quantidadeEstoque nao pode ser negativa")
        Integer quantidadeEstoque,

        @NotBlank(message = "unidadeMedida e obrigatoria")
        @Size(max = 10, message = "unidadeMedida deve ter no maximo 10 caracteres")
        String unidadeMedida
) {}
