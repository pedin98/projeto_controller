package com.expedicao.controle.dto.material;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/** Ajuste manual de saldo (entrada de mercadoria, inventario). */
public record EstoqueUpdateDTO(

        @NotNull(message = "quantidadeEstoque e obrigatoria")
        @PositiveOrZero(message = "quantidadeEstoque nao pode ser negativa")
        Integer quantidadeEstoque
) {}
