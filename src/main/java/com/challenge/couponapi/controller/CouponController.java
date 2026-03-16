package com.challenge.couponapi.controller;

import com.challenge.couponapi.dto.CreateCouponRequest;
import com.challenge.couponapi.dto.CouponResponse;
import com.challenge.couponapi.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService service;

    @PostMapping
    public ResponseEntity<CouponResponse> create(
            @Valid @RequestBody CreateCouponRequest request) {

        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CouponResponse> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
