package com.expedicao.controle.service;

import com.expedicao.controle.domain.Expedicao;
import com.expedicao.controle.domain.Material;
import com.expedicao.controle.domain.enums.StatusExpedicao;
import com.expedicao.controle.dto.expedicao.ExpedicaoRequestDTO;
import com.expedicao.controle.dto.expedicao.ExpedicaoResponseDTO;
import com.expedicao.controle.exception.EstoqueInsuficienteException;
import com.expedicao.controle.exception.OperacaoInvalidaException;
import com.expedicao.controle.exception.RecursoNaoEncontradoException;
import com.expedicao.controle.repository.ExpedicaoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpedicaoService {

    private final ExpedicaoRepository expedicaoRepository;
    private final MaterialService materialService;

    /**
     * Cria a expedicao no status PENDENTE.
     * O estoque NAO e baixado aqui - apenas validado como checagem antecipada.
     * A baixa ocorre somente na conclusao.
     */
    @Transactional
    public ExpedicaoResponseDTO criar(ExpedicaoRequestDTO dto) {
        Material material = materialService.buscarEntidade(dto.materialId());

        if (material.getQuantidadeEstoque() < dto.quantidadeExpedida()) {
            throw new EstoqueInsuficienteException(
                    "Estoque insuficiente para o material '" + material.getSku()
                            + "'. Disponivel: " + material.getQuantidadeEstoque()
                            + ", solicitado: " + dto.quantidadeExpedida());
        }

        Expedicao expedicao = Expedicao.builder()
                .material(material)
                .quantidadeExpedida(dto.quantidadeExpedida())
                .destinatario(dto.destinatario())
                .status(StatusExpedicao.PENDENTE) // reforco explicito; @PrePersist garante o default
                .build();

        return ExpedicaoResponseDTO.from(expedicaoRepository.save(expedicao));
    }

    public List<ExpedicaoResponseDTO> listar(StatusExpedicao status) {
        List<Expedicao> resultado = (status == null)
                ? expedicaoRepository.findAll()
                : expedicaoRepository.findByStatus(status);
        return resultado.stream().map(ExpedicaoResponseDTO::from).toList();
    }

    public ExpedicaoResponseDTO buscarPorId(Long id) {
        return ExpedicaoResponseDTO.from(buscarEntidade(id));
    }

    /**
     * Confirma a expedicao: valida o estoque no momento da conclusao e executa a baixa.
     * So e permitido a partir do status PENDENTE.
     */
    @Transactional
    public ExpedicaoResponseDTO concluir(Long id) {
        Expedicao expedicao = buscarEntidade(id);
        exigirStatus(expedicao, StatusExpedicao.PENDENTE, "concluida");

        Material material = expedicao.getMaterial();
        int saldo = material.getQuantidadeEstoque();
        int quantidade = expedicao.getQuantidadeExpedida();

        if (saldo < quantidade) {
            throw new EstoqueInsuficienteException(
                    "Estoque insuficiente para concluir a expedicao " + id
                            + ". Disponivel: " + saldo + ", necessario: " + quantidade);
        }

        material.setQuantidadeEstoque(saldo - quantidade); // baixa automatica no estoque
        expedicao.setStatus(StatusExpedicao.CONCLUIDO);
        return ExpedicaoResponseDTO.from(expedicao); // dirty checking persiste material + expedicao
    }

    /** Cancela uma expedicao ainda PENDENTE. Nao ha estoque a estornar. */
    @Transactional
    public ExpedicaoResponseDTO cancelar(Long id) {
        Expedicao expedicao = buscarEntidade(id);
        exigirStatus(expedicao, StatusExpedicao.PENDENTE, "cancelada");
        expedicao.setStatus(StatusExpedicao.CANCELADO);
        return ExpedicaoResponseDTO.from(expedicao);
    }

    private Expedicao buscarEntidade(Long id) {
        return expedicaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Expedicao nao encontrada para o id " + id));
    }

    private void exigirStatus(Expedicao expedicao, StatusExpedicao esperado, String acao) {
        if (expedicao.getStatus() != esperado) {
            throw new OperacaoInvalidaException(
                    "Expedicao " + expedicao.getId() + " esta " + expedicao.getStatus()
                            + " e nao pode ser " + acao + ". Status exigido: " + esperado);
        }
    }
}
