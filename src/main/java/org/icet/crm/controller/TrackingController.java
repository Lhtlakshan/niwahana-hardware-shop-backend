package org.icet.crm.controller;

import lombok.RequiredArgsConstructor;
import org.icet.crm.dto.OrderDto;
import org.icet.crm.service.customer.cart.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/admin")
public class TrackingController {

    private final CartService cartService;

    @GetMapping("/order/{trackingId}")
    public ResponseEntity<OrderDto> searchOrderByTrackingId(@PathVariable UUID trackingId){
        OrderDto orderDto = cartService.searchOrderByTrackingId(trackingId);
        if(orderDto == null){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDto);
    }
}
