package com.challenge.couponapi.domain;

import com.challenge.couponapi.exception.BusinessException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    @DisplayName("Deve remover caracteres especiais do código ao criar um cupom")
    void deveSanitizeCode() {

        Coupon coupon = Coupon.create(
                "ABC-123",
                "test",
                1.0,
                LocalDateTime.now().plusDays(1),
                false
        );

        assertEquals("ABC123", coupon.getCode());
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando a data de expiração for no passado")
    void deveThrowExceptionWhenExpirationDateIsPast() {

        assertThrows(
                BusinessException.class,
                () -> Coupon.create(
                        "ABC123",
                        "test",
                        1.0,
                        LocalDateTime.now().minusDays(1),
                        false
                )
        );
    }
}