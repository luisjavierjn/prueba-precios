package com.inditex.pruebaprecios.services;

import com.inditex.pruebaprecios.domain.dto.PriceDTO;
import com.inditex.pruebaprecios.domain.entities.Price;
import com.inditex.pruebaprecios.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceServices {
    private final PriceRepository priceRepository;
    private final ModelMapper modelMapper;

    public List<PriceDTO> getAllPrices() {
        return priceRepository.findAll()
                .stream()
                .map(price -> modelMapper.map(price, PriceDTO.class))
                .collect(Collectors.toList());
    }
}
