package com.inditex.pruebaprecios.domain.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CreatePriceRequest {
    private LocalDateTime applicationDate;
    private Integer productId;
    @NotEmpty(message = "Brand Id should not be empty")
    private String brandId;
}
