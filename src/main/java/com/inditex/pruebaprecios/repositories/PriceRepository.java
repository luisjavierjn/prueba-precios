package com.inditex.pruebaprecios.repositories;

import com.inditex.pruebaprecios.domain.entities.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> { }
