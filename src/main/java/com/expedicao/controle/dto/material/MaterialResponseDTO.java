package com.expedicao.controle.dto.material;

import com.expedicao.controle.domain.Material;

public record MaterialResponseDTO(
        Long id,
        String nome,
        String sku,
        Integer quantidadeEstoque,
        String unidadeMedida
) {
    public static MaterialResponseDTO from(Material material) {
        return new MaterialResponseDTO(
                material.getId(),
                material.getNome(),
                material.getSku(),
                material.getQuantidadeEstoque(),
                material.getUnidadeMedida()
        );
    }
}
