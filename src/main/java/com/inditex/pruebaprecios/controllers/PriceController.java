package com.inditex.pruebaprecios.controllers;

import com.inditex.pruebaprecios.domain.dto.OutputDTO;
import com.inditex.pruebaprecios.domain.dto.PriceDTO;
import com.inditex.pruebaprecios.domain.requests.CreatePriceRequest;
import com.inditex.pruebaprecios.services.PriceServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    public ResponseEntity<?> getOutputData(@RequestBody @Valid CreatePriceRequest request) {
        try {
            Integer.parseInt(request.getBrandId());
            return ResponseEntity.ok(priceServices.getOutputData(request));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Brand Id does not contain a valid number");
        }
    }
}
