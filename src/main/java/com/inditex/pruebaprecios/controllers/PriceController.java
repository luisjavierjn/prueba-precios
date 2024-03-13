package com.inditex.pruebaprecios.controllers;

import com.inditex.pruebaprecios.domain.dto.PriceDTO;
import com.inditex.pruebaprecios.services.PriceServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PriceController {
    private final PriceServices priceServices;

    @GetMapping
    public ResponseEntity<List<PriceDTO>> getAllPrices() {
        return ResponseEntity.ok(priceServices.getAllPrices());
    }
}
