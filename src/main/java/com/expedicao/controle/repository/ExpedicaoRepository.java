package com.expedicao.controle.repository;

import com.expedicao.controle.domain.Expedicao;
import com.expedicao.controle.domain.enums.StatusExpedicao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpedicaoRepository extends JpaRepository<Expedicao, Long> {

    List<Expedicao> findByStatus(StatusExpedicao status);

    List<Expedicao> findByMaterialId(Long materialId);

    boolean existsByMaterialIdAndStatus(Long materialId, StatusExpedicao status);
}
