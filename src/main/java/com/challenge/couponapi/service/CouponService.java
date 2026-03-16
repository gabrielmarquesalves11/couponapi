package com.challenge.couponapi.service;

import com.challenge.couponapi.domain.Coupon;
import com.challenge.couponapi.dto.CreateCouponRequest;
import com.challenge.couponapi.dto.CouponResponse;
import com.challenge.couponapi.exception.BusinessException;
import com.challenge.couponapi.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository repository;

    public CouponResponse create(CreateCouponRequest request) {

        Coupon coupon = Coupon.create(
                request.getCode(),
                request.getDescription(),
                request.getDiscountValue(),
                request.getExpirationDate(),
                request.isPublished());

        repository.save(coupon);

        return toResponse(coupon);
    }

    public CouponResponse findById(UUID id) {

        Coupon coupon = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Coupon não encontrado."));

        return toResponse(coupon);
    }

    public CouponResponse delete(UUID id) {

        Coupon coupon = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Coupon não encontrado."));

        coupon.delete();

        repository.save(coupon);

        return toResponse(coupon);
    }

    private CouponResponse toResponse(Coupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getCode(), coupon.getDescription(), coupon.getDiscountValue(),
                coupon.getExpirationDate(), coupon.getStatus().name(), coupon.isPublished(), coupon.isRedeemed());
    }
}
