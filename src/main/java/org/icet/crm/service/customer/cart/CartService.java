package org.icet.crm.service.customer.cart;

import org.icet.crm.dto.AddProductInCartDto;
import org.icet.crm.dto.OrderDto;
import org.springframework.http.ResponseEntity;

public interface CartService {
    ResponseEntity<?> addProductToCart(AddProductInCartDto addProductCartDto);
    OrderDto getCartByUserId(Long userId);
    public OrderDto applyCoupon(Long userId,String code);
}
