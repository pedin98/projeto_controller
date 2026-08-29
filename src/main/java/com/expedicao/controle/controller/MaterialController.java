package com.expedicao.controle.controller;

import com.expedicao.controle.dto.material.EstoqueUpdateDTO;
import com.expedicao.controle.dto.material.MaterialRequestDTO;
import com.expedicao.controle.dto.material.MaterialResponseDTO;
import com.expedicao.controle.service.MaterialService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/materiais")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping
    public ResponseEntity<MaterialResponseDTO> criar(
            @Valid @RequestBody MaterialRequestDTO dto,
            UriComponentsBuilder uriBuilder) {

        MaterialResponseDTO criado = materialService.criar(dto);
        URI location = uriBuilder.path("/api/materiais/{id}")
                .buildAndExpand(criado.id())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping
    public List<MaterialResponseDTO> listar() {
        return materialService.listarTodos();
    }

    @GetMapping("/{id}")
    public MaterialResponseDTO buscarPorId(@PathVariable Long id) {
        return materialService.buscarPorId(id);
    }

    @PatchMapping("/{id}/estoque")
    public MaterialResponseDTO atualizarEstoque(
            @PathVariable Long id,
            @Valid @RequestBody EstoqueUpdateDTO dto) {
        return materialService.atualizarEstoque(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        materialService.deletar(id);
    }
}
