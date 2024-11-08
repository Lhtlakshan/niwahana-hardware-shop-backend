package org.icet.crm.service.customer.cart;

import org.icet.crm.dto.AddProductInCartDto;
import org.icet.crm.dto.OrderDto;
import org.icet.crm.dto.PlaceOrderDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CartService {
    ResponseEntity<?> addProductToCart(AddProductInCartDto addProductCartDto);
    OrderDto getCartByUserId(Long userId);
    OrderDto applyCoupon(Long userId,String code);
    OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto);
    OrderDto placeOrder(PlaceOrderDto placeOrderDto);
    List<OrderDto> getCustomerPlacedOrders(Long userId);
    OrderDto searchOrderByTrackingId(UUID trackingId);
}
