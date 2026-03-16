package com.challenge.couponapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.challenge.couponapi.dto.CouponResponse;
import com.challenge.couponapi.dto.CreateCouponRequest;
import com.challenge.couponapi.service.CouponService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CouponController.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponService couponService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarCupomComSucesso() throws Exception {
        CreateCouponRequest request = new CreateCouponRequest("ABC123", "Desconto", 10.0, LocalDateTime.now().plusDays(1), false);
        CouponResponse response = new CouponResponse(UUID.randomUUID(), "ABC123", "Desconto", 10.0, null, null, false, false);

        when(couponService.create(any())).thenReturn(response);

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ABC123"));
    }

    @Test
    void deveBuscarCupomPorId() throws Exception {
        UUID id = UUID.randomUUID();
        CouponResponse response = new CouponResponse(id, "ABC123", "Desconto", 10.0, null, null, false, false);

        when(couponService.findById(id)).thenReturn(response);

        mockMvc.perform(get("/coupon/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void deveDeletarCupomComSucesso() throws Exception {
        UUID id = UUID.randomUUID();
        CouponResponse response = new CouponResponse(id, "ABC123", "Desconto", 10.0, null, null, false, false);

        when(couponService.delete(id)).thenReturn(response);

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(couponService, times(1)).delete(id);
    }

    @Test
    void deveRetornarErroAoTentarBuscarCupomInexistente() throws Exception {
        UUID id = UUID.randomUUID();
        when(couponService.findById(id)).thenThrow(new com.challenge.couponapi.exception.BusinessException("Coupon not found"));

        mockMvc.perform(get("/coupon/{id}", id))
                .andExpect(status().isBadRequest()); 
    }
}