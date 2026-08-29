package com.expedicao.controle.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "material")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    /** Saldo atual em estoque. Nunca pode ficar negativo (validado no Service). */
    @Column(nullable = false)
    private Integer quantidadeEstoque;

    /** Ex.: UN, KG, M, CX. */
    @Column(name = "unidade_medida", nullable = false, length = 10)
    private String unidadeMedida;

    /** Controle de concorrencia otimista para a baixa de estoque. */
    @Version
    private Long version;
}
