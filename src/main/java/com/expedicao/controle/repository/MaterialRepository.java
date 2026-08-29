package com.expedicao.controle.repository;

import com.expedicao.controle.domain.Material;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    Optional<Material> findBySku(String sku);

    boolean existsBySku(String sku);
}
