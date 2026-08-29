package com.expedicao.controle.dto.expedicao;

import com.expedicao.controle.domain.Expedicao;
import com.expedicao.controle.domain.enums.StatusExpedicao;
import com.expedicao.controle.dto.material.MaterialResponseDTO;
import java.time.LocalDateTime;

public record ExpedicaoResponseDTO(
        Long id,
        MaterialResponseDTO material,
        Integer quantidadeExpedida,
        LocalDateTime dataCriacao,
        String destinatario,
        StatusExpedicao status
) {
    public static ExpedicaoResponseDTO from(Expedicao expedicao) {
        return new ExpedicaoResponseDTO(
                expedicao.getId(),
                MaterialResponseDTO.from(expedicao.getMaterial()),
                expedicao.getQuantidadeExpedida(),
                expedicao.getDataCriacao(),
                expedicao.getDestinatario(),
                expedicao.getStatus()
        );
    }
}
