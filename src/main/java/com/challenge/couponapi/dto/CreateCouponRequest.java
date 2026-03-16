package com.challenge.couponapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CreateCouponRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String description;

    @NotNull
    private Double discountValue;

    @NotNull
    private LocalDateTime expirationDate;

    private boolean published;
}