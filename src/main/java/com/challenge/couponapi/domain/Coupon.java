package com.challenge.couponapi.domain;

import com.challenge.couponapi.exception.BusinessException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "coupons")
@Where(clause = "deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String code;
    private String description;
    private Double discountValue;
    private LocalDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    private boolean published;
    private boolean redeemed;
    private boolean deleted;

    public static Coupon create(
            String code,
            String description,
            Double discountValue,
            LocalDateTime expirationDate,
            boolean published
    ) {

        validateExpirationDate(expirationDate);
        validateDiscountValue(discountValue);

        String sanitizedCode = sanitizeCode(code);

        Coupon coupon = new Coupon();
        coupon.code = sanitizedCode;
        coupon.description = description;
        coupon.discountValue = discountValue;
        coupon.expirationDate = expirationDate;
        coupon.published = published;
        coupon.status = CouponStatus.ACTIVE;
        coupon.deleted = false;
        coupon.redeemed = false;

        return coupon;
    }

    public void delete() {
        if (this.deleted) {
            throw new BusinessException("Coupon already deleted");
        }
        this.deleted = true;
    }

    private static void validateExpirationDate(LocalDateTime expirationDate) {
        // Se a data for nula, assume que não tem validade ou trate como válido
        if (expirationDate != null && expirationDate.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Data de expiração não pode ser nula e nem anterior à data atual.");
        }
    }

    private static void validateDiscountValue(Double value) {
        if (value < 0.5) {
            throw new BusinessException("Desconto mínimo é de 0.5");
        }
    }

    private static String sanitizeCode(String code) {

        String sanitized =
                code.replaceAll("[^a-zA-Z0-9]", "")
                        .toUpperCase();

        if (sanitized.length() != 6) {
            throw new BusinessException("Código do coupon deve ter 6 caracteres.");
        }

        return sanitized;
    }
}