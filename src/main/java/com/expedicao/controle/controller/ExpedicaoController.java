package com.expedicao.controle.controller;

import com.expedicao.controle.domain.enums.StatusExpedicao;
import com.expedicao.controle.dto.expedicao.ExpedicaoRequestDTO;
import com.expedicao.controle.dto.expedicao.ExpedicaoResponseDTO;
import com.expedicao.controle.service.ExpedicaoService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/expedicoes")
@RequiredArgsConstructor
public class ExpedicaoController {

    private final ExpedicaoService expedicaoService;

    @PostMapping
    public ResponseEntity<ExpedicaoResponseDTO> criar(
            @Valid @RequestBody ExpedicaoRequestDTO dto,
            UriComponentsBuilder uriBuilder) {

        ExpedicaoResponseDTO criada = expedicaoService.criar(dto);
        URI location = uriBuilder.path("/api/expedicoes/{id}")
                .buildAndExpand(criada.id())
                .toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @GetMapping
    public List<ExpedicaoResponseDTO> listar(
            @RequestParam(required = false) StatusExpedicao status) {
        return expedicaoService.listar(status);
    }

    @GetMapping("/{id}")
    public ExpedicaoResponseDTO buscarPorId(@PathVariable Long id) {
        return expedicaoService.buscarPorId(id);
    }

    /** Confirma a expedicao e da baixa no estoque. */
    @PatchMapping("/{id}/conclusao")
    public ExpedicaoResponseDTO concluir(@PathVariable Long id) {
        return expedicaoService.concluir(id);
    }

    /** Cancela uma expedicao PENDENTE. */
    @PatchMapping("/{id}/cancelamento")
    public ExpedicaoResponseDTO cancelar(@PathVariable Long id) {
        return expedicaoService.cancelar(id);
    }
}
