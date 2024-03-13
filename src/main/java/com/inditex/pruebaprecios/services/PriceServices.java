package com.inditex.pruebaprecios.services;

import com.inditex.pruebaprecios.domain.dto.OutputDTO;
import com.inditex.pruebaprecios.domain.dto.PriceDTO;
import com.inditex.pruebaprecios.domain.requests.CreatePriceRequest;
import com.inditex.pruebaprecios.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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


    public List<OutputDTO> getOutputData(CreatePriceRequest request) {
        return priceRepository.findApplicablePrice( request.getApplicationDate(),
                                                    request.getProductId(),
                                                    Integer.parseInt(request.getBrandId()));
    }
}
