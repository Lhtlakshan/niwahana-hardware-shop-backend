package org.icet.crm.service.customer.cart.impl;

import lombok.RequiredArgsConstructor;
import org.icet.crm.dto.AddProductInCartDto;
import org.icet.crm.dto.CartItemDto;
import org.icet.crm.dto.OrderDto;
import org.icet.crm.dto.PlaceOrderDto;
import org.icet.crm.entity.*;
import org.icet.crm.enums.OrderStatus;
import org.icet.crm.exceptions.ValidationException;
import org.icet.crm.repository.*;
import org.icet.crm.service.customer.cart.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final CartItemsRepository cartItemsRepository;

    private final ProductRepository productRepository;

    private final CouponRepository couponRepository;

    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.PENDING);
        Optional<CartItems> optionalCartItems = cartItemsRepository.findByProductIdAndOrderIdAndUserId(addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());

        if (optionalCartItems.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
            Optional<User> optionalUser = userRepository.findById(addProductInCartDto.getUserId());

            if (optionalProduct.isPresent() && optionalUser.isPresent()) {
                CartItems cart = new CartItems();
                cart.setProduct(optionalProduct.get());
                cart.setPrice(optionalProduct.get().getPrice());
                cart.setQuantity(1L);
                cart.setUser(optionalUser.get());
                cart.setOrder(activeOrder);

                CartItems updatedCart = cartItemsRepository.save(cart);

                activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cart.getPrice());
                activeOrder.setAmount(activeOrder.getAmount() + cart.getPrice());
                activeOrder.getCartItems().add(cart);

                orderRepository.save(activeOrder);

                return ResponseEntity.status(HttpStatus.CREATED).body(cart);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or product not found");
            }

        }
    }

    public OrderDto getCartByUserId(Long userId){
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING);
        List<CartItemDto> cartItemDtoList = activeOrder.getCartItems().stream().map(CartItems::getCartDto).collect(Collectors.toList());
        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(activeOrder.getAmount());
        orderDto.setId(activeOrder.getId());
        orderDto.setOrderStatus(activeOrder.getOrderStatus());
        orderDto.setDiscount(activeOrder.getDiscount());
        orderDto.setTotalAmount(activeOrder.getTotalAmount());
        orderDto.setCartItems(cartItemDtoList);

        if(activeOrder.getCoupon() != null){
            orderDto.setCouponName(activeOrder.getCoupon().getName());
        }

        return orderDto;
    }

    public OrderDto applyCoupon(Long userId,String code){
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING);
        Coupon coupon = couponRepository.findByCode(code).orElseThrow(()-> new ValidationException("Coupon not found"));

        if(couponIsExpiration(coupon)){
            throw new ValidationException("Coupon expired...");
        }

        double discountAmount = ((coupon.getDiscount()/100.0)*activeOrder.getTotalAmount());
        double netAmount = activeOrder.getTotalAmount()-discountAmount;

        activeOrder.setAmount((long)netAmount);
        activeOrder.setDiscount((long)discountAmount);
        activeOrder.setCoupon(coupon);

        orderRepository.save(activeOrder);
        return modelMapper.map(activeOrder,OrderDto.class);
    }

    private boolean couponIsExpiration(Coupon coupon){
        Date currentDate = new Date();
        Date expirationDate = coupon.getExpirationDate();

        return expirationDate != null && currentDate.after(expirationDate);
    }

    public OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto){
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.PENDING);
        Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());

        Optional<CartItems> optionalCartItems = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
                addProductInCartDto.getProductId(),
                activeOrder.getId(),
                addProductInCartDto.getUserId()
        );

        if(optionalProduct.isPresent() && optionalCartItems.isPresent()){
            CartItems cartItems = optionalCartItems.get();
            Product product = optionalProduct.get();

            activeOrder.setAmount(activeOrder.getAmount()+product.getPrice());
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() + +product.getPrice());

            cartItems.setQuantity(cartItems.getQuantity() + 1);
            System.out.println(cartItems.getQuantity() );

            if(activeOrder.getCoupon() !=null){
                double discountAmount = ((activeOrder.getCoupon().getDiscount()/100.0)*activeOrder.getTotalAmount());
                double netAmount = activeOrder.getTotalAmount()-discountAmount;

                activeOrder.setAmount((long)netAmount);
                activeOrder.setDiscount((long)discountAmount);

                cartItemsRepository.save(cartItems);
                orderRepository.save(activeOrder);

                return modelMapper.map(activeOrder,OrderDto.class);
            }
        }
        return null;
    }

    public OrderDto placeOrder(PlaceOrderDto placeOrderDto){
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(placeOrderDto.getUserId(), OrderStatus.PENDING);
        Optional<User> optionalUser = userRepository.findById(placeOrderDto.getUserId());
        if(optionalUser.isPresent()){
            activeOrder.setOrderDescription(placeOrderDto.getOrderDescription());
            activeOrder.setAddress(placeOrderDto.getAddress());
            activeOrder.setDate(new Date());
            activeOrder.setOrderStatus(OrderStatus.PLACED);
            activeOrder.setTrackingId(UUID.randomUUID());

            orderRepository.save(activeOrder);

            Order order= new Order();
            order.setAmount(0L);
            order.setTotalAmount(0L);
            order.setDiscount(0L);
            order.setUser(optionalUser.get());
            order.setOrderStatus(OrderStatus.PENDING);
            orderRepository.save(order);

            return modelMapper.map(activeOrder,OrderDto.class);
        }
        return null;
    }

    public List<OrderDto> getCustomerPlacedOrders(Long userId){
        List<OrderStatus> orderStatusList = List.of(OrderStatus.PLACED,OrderStatus.DELIVERED,OrderStatus.SHIPPED);
        List<Order> orderList = orderRepository.findAllByUserIdAndOrderStatusIn(userId,orderStatusList);
        List<OrderDto> orderDtoList = new ArrayList<>();
        orderList.forEach(order -> {
            orderDtoList.add(modelMapper.map(order, OrderDto.class));
        });

        return orderDtoList;
    }

    public OrderDto searchOrderByTrackingId(UUID trackingId){
        Optional<Order> optionalOrder = orderRepository.findByTrackingId(trackingId);
        if(optionalOrder.isPresent()){
            return modelMapper.map(optionalOrder.get(),OrderDto.class);
        }else{
            return null;
        }
    }
}
