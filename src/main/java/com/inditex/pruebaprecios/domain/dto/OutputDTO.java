package com.inditex.pruebaprecios.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
public class OutputDTO {

    public OutputDTO(Integer productId, Integer brandId, Integer priceList, LocalDateTime startDate, LocalDateTime endDate, BigDecimal price) {
        this.productId = productId;
        this.brandId = brandId;
        this.priceList = priceList;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }

    private Integer productId;
    @JsonProperty("chainIdentifier")
    private Integer brandId; // chain identifier
    @JsonProperty("rateToApply")
    private Integer priceList; // rate to apply
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @JsonProperty("finalPrice")
    private BigDecimal price; // final price
}
