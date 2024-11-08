package org.icet.crm.service.admin.adminOrder.impl;

import lombok.RequiredArgsConstructor;
import org.icet.crm.dto.OrderDto;
import org.icet.crm.entity.Order;
import org.icet.crm.enums.OrderStatus;
import org.icet.crm.repository.OrderRepository;
import org.icet.crm.service.admin.adminOrder.AdminOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminOrderImpl implements AdminOrderService {

    private final OrderRepository orderRepository;

    private final ModelMapper modelMapper;

    public List<OrderDto> getAllPlacedOrders(){
        List<Order> orderList = orderRepository.findAllByOrderStatusIn(List.of(OrderStatus.PLACED,OrderStatus.DELIVERED,OrderStatus.SHIPPED));
        List<OrderDto> orderDtoList = new ArrayList<>();
        orderList.forEach(order -> {
            orderDtoList.add(modelMapper.map(order,OrderDto.class));
        });

        return orderDtoList;
    }

    public OrderDto changeOrderStatus(Long orderId , String status){
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.isPresent()){
            Order order = optionalOrder.get();

            if(Objects.equals(status , "SHIPPED")){
                order.setOrderStatus(OrderStatus.SHIPPED);
            }else if(Objects.equals(status , "DELIVERED")){
                order.setOrderStatus(OrderStatus.DELIVERED);
            }
            return modelMapper.map(orderRepository.save(order) , OrderDto.class);
        }
        return null;
    }
}
