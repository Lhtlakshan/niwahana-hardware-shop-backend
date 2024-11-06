package org.icet.crm.controller.customer;

import lombok.RequiredArgsConstructor;
import org.icet.crm.dto.AddProductInCartDto;
import org.icet.crm.dto.OrderDto;
import org.icet.crm.service.customer.cart.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addProductToCart(@RequestBody AddProductInCartDto addProductCartDto){
        return cartService.addProductToCart(addProductCartDto);
    }

    @GetMapping("/get-cart/{userId}")
    public ResponseEntity<?> getCartByUserId(@PathVariable Long userId){
        OrderDto orderDto = cartService.getCartByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(orderDto);
    }
}
