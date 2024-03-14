package com.inditex.pruebaprecios.services;

import com.inditex.pruebaprecios.domain.dto.ApplicableRangeDTO;
import com.inditex.pruebaprecios.domain.dto.PriceDTO;
import com.inditex.pruebaprecios.domain.requests.CreatePriceRequest;
import com.inditex.pruebaprecios.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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


    public List<ApplicableRangeDTO> getApplicableRanges(CreatePriceRequest request) {
        return priceRepository.findApplicableRanges(request.getApplicationDate(),
                                                    request.getProductId(),
                                                    Integer.parseInt(request.getBrandId()));
    }

    public Optional<ApplicableRangeDTO> getOutputData(CreatePriceRequest request) {
        List<PriceDTO> priceDTOs = getAllPrices();

        Optional<ApplicableRangeDTO> applicableRangeDTO = priceDTOs.stream()
                .filter(p ->
                       (request.getApplicationDate().isAfter(p.getStartDate()) || request.getApplicationDate().isEqual(p.getStartDate())) &&
                       (request.getApplicationDate().isBefore(p.getEndDate()) || request.getApplicationDate().isEqual(p.getEndDate())) &&
                        request.getBrandId().equals(p.getBrandId().toString()) &&
                        request.getProductId() == p.getProductId())
                .max(Comparator.comparing(PriceDTO::getPriority))
                .map(p -> new ApplicableRangeDTO(p.getProductId(),
                        p.getBrandId(),
                        p.getPriceList(),
                        p.getStartDate(),
                        p.getEndDate(),
                        p.getPrice(),
                        p.getPriority()));

        applicableRangeDTO.ifPresent(r -> {
            Optional<PriceDTO> leftPrice = priceDTOs.stream()
                    .filter(p -> p.getPriority() > r.getPriority() &&
                            p.getEndDate().isBefore(request.getApplicationDate()) &&
                            p.getEndDate().isAfter(r.getStartDate()))
                    .max(Comparator.comparing(PriceDTO::getEndDate));
            leftPrice.ifPresent(p -> {
                r.setStartDate(p.getEndDate());
            });

            Optional<PriceDTO> rightPrice = priceDTOs.stream()
                    .filter(p -> p.getPriority() > r.getPriority() &&
                            p.getStartDate().isAfter(request.getApplicationDate()) &&
                            p.getStartDate().isBefore(r.getEndDate()))
                    .min(Comparator.comparing(PriceDTO::getEndDate));
            rightPrice.ifPresent(p -> {
                r.setEndDate(p.getStartDate());
            });
        });

        return applicableRangeDTO;
    }
}
