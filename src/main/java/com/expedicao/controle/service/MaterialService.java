package com.expedicao.controle.service;

import com.expedicao.controle.domain.Material;
import com.expedicao.controle.domain.enums.StatusExpedicao;
import com.expedicao.controle.dto.material.EstoqueUpdateDTO;
import com.expedicao.controle.dto.material.MaterialRequestDTO;
import com.expedicao.controle.dto.material.MaterialResponseDTO;
import com.expedicao.controle.exception.OperacaoInvalidaException;
import com.expedicao.controle.exception.RecursoNaoEncontradoException;
import com.expedicao.controle.repository.ExpedicaoRepository;
import com.expedicao.controle.repository.MaterialRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final ExpedicaoRepository expedicaoRepository;

    @Transactional
    public MaterialResponseDTO criar(MaterialRequestDTO dto) {
        if (materialRepository.existsBySku(dto.sku())) {
            throw new OperacaoInvalidaException("Ja existe material com o SKU '" + dto.sku() + "'");
        }
        Material material = Material.builder()
                .nome(dto.nome())
                .sku(dto.sku())
                .quantidadeEstoque(dto.quantidadeEstoque())
                .unidadeMedida(dto.unidadeMedida())
                .build();
        return MaterialResponseDTO.from(materialRepository.save(material));
    }

    public List<MaterialResponseDTO> listarTodos() {
        return materialRepository.findAll().stream()
                .map(MaterialResponseDTO::from)
                .toList();
    }

    public MaterialResponseDTO buscarPorId(Long id) {
        return MaterialResponseDTO.from(buscarEntidade(id));
    }

    /** Ajuste absoluto de saldo (entrada de mercadoria, correcao de inventario). */
    @Transactional
    public MaterialResponseDTO atualizarEstoque(Long id, EstoqueUpdateDTO dto) {
        Material material = buscarEntidade(id);
        material.setQuantidadeEstoque(dto.quantidadeEstoque());
        return MaterialResponseDTO.from(material); // dirty checking persiste na transacao
    }

    @Transactional
    public void deletar(Long id) {
        Material material = buscarEntidade(id);
        if (expedicaoRepository.existsByMaterialIdAndStatus(id, StatusExpedicao.PENDENTE)) {
            throw new OperacaoInvalidaException(
                    "Material nao pode ser removido: possui expedicoes PENDENTE vinculadas");
        }
        materialRepository.delete(material);
    }

    /** Uso interno / pelo ExpedicaoService. */
    Material buscarEntidade(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Material nao encontrado para o id " + id));
    }
}
