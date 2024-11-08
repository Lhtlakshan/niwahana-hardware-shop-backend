package org.icet.crm.controller.customer;

import lombok.RequiredArgsConstructor;
import org.icet.crm.dto.AddProductInCartDto;
import org.icet.crm.dto.OrderDto;
import org.icet.crm.dto.PlaceOrderDto;
import org.icet.crm.exceptions.ValidationException;
import org.icet.crm.service.customer.cart.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("coupon/{userId}/{code}")
    public ResponseEntity<?> applyCoupon(@PathVariable Long userId,@PathVariable String code){
        try{
            OrderDto orderDto = cartService.applyCoupon(userId,code);
            return ResponseEntity.ok(orderDto);
        }catch(ValidationException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/add-product")
    public ResponseEntity<OrderDto> increaseProductQuantity(@RequestBody AddProductInCartDto addProductCartDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.increaseProductQuantity(addProductCartDto));
    }

    @PostMapping("/place-order")
    public ResponseEntity<OrderDto> placeOrder(@RequestBody PlaceOrderDto placeOrderDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.placeOrder(placeOrderDto));
    }

    @GetMapping("/placed-orders/{userId}")
    public ResponseEntity<List<OrderDto>> getCustomerPlacedOrders(@PathVariable Long userId){
        return ResponseEntity.ok(cartService.getCustomerPlacedOrders(userId));
    }
}
