package com.challenge.couponapi.service;

import com.challenge.couponapi.domain.Coupon;
import com.challenge.couponapi.dto.CouponResponse;
import com.challenge.couponapi.dto.CreateCouponRequest;
import com.challenge.couponapi.exception.BusinessException;
import com.challenge.couponapi.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository repository;

    @InjectMocks
    private CouponService service;

    @Test
    void deveCriarCupomComSucesso() {
        CreateCouponRequest request = new CreateCouponRequest("DESC10", "Desconto 10%", 10.0, null, true);
        
        CouponResponse response = service.create(request);

        assertNotNull(response);
        assertEquals("DESC10", response.getCode());
        verify(repository, times(1)).save(any(Coupon.class));
    }

    @Test
    void deveBuscarCupomPorIdComSucesso() {
        UUID id = UUID.randomUUID();
        Coupon coupon = Coupon.create("123456", "Desc", 10.0, null, true);
        when(repository.findById(id)).thenReturn(Optional.of(coupon));

        CouponResponse response = service.findById(id);

        assertNotNull(response);
        assertEquals("123456", response.getCode());
    }

    @Test
    void deveLancarExcecaoQuandoCupomNaoEncontrado() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.findById(id));
    }

    @Test
    void deveDeletarCupomComSucesso() {
        UUID id = UUID.randomUUID();
        Coupon coupon = Coupon.create("123456", "Desc", 10.0, null, true);
        
        when(repository.findById(id)).thenReturn(Optional.of(coupon));
        when(repository.save(any(Coupon.class))).thenReturn(coupon);

        CouponResponse response = service.delete(id);

        assertNotNull(response);
        verify(repository, times(1)).save(any(Coupon.class));
    }

    @Test
    void deveLancarExcecaoAoTentarDeletarCupomInexistente() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.delete(id));
        verify(repository, never()).save(any(Coupon.class));
    }
}
