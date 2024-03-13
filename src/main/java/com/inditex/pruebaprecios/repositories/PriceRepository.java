package com.inditex.pruebaprecios.repositories;

import com.inditex.pruebaprecios.domain.dto.OutputDTO;
import com.inditex.pruebaprecios.domain.entities.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    @Query("SELECT new com.inditex.pruebaprecios.domain.dto.OutputDTO(p.productId, p.brandId, p.priceList, p.startDate, p.endDate, p.price) " +
            "FROM Price p " +
            "WHERE :applicationDate BETWEEN p.startDate AND p.endDate " +
            "AND p.productId = :productId " +
            "AND p.brandId = :brandId " +
            "ORDER BY p.priority DESC")
    List<OutputDTO> findApplicablePrice(@Param("applicationDate") LocalDateTime applicationDate,
                                        @Param("productId") Integer productId,
                                        @Param("brandId") Integer brandId);
}
