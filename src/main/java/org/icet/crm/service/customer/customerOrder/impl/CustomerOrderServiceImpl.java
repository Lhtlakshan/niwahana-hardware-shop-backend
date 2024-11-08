package org.icet.crm.service.customer.customerOrder.impl;

import lombok.RequiredArgsConstructor;
import org.icet.crm.dto.OrderDto;
import org.icet.crm.entity.Order;
import org.icet.crm.enums.OrderStatus;
import org.icet.crm.repository.OrderRepository;
import org.icet.crm.service.customer.customerOrder.CustomerOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerOrderServiceImpl implements CustomerOrderService {

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
}
