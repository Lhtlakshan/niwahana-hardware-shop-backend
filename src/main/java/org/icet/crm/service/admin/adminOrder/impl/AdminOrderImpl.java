package org.icet.crm.service.admin.adminOrder.impl;

import lombok.RequiredArgsConstructor;
import org.icet.crm.dto.AnalyticsResponseDto;
import org.icet.crm.dto.OrderDto;
import org.icet.crm.entity.Order;
import org.icet.crm.enums.OrderStatus;
import org.icet.crm.repository.OrderRepository;
import org.icet.crm.service.admin.adminOrder.AdminOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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

    public AnalyticsResponseDto calculateAnalytics(){
        LocalDate currentDate = LocalDate.now();
        LocalDate previousDate = currentDate.minusMonths(1);

        Long currentMonthOrders = getTotalOrdersForMonth(currentDate.getMonthValue(),currentDate.getYear());
        Long previousMonthOrders = getTotalOrdersForMonth(previousDate.getMonthValue(),previousDate.getYear());

        Long currentMonthEarnings = getTotalEarningForMonth(currentDate.getMonthValue(),currentDate.getYear());
        Long previousMonthEarnings = getTotalEarningForMonth(previousDate.getMonthValue(),previousDate.getYear());

        Long placed = orderRepository.countByOrderStatus(OrderStatus.PLACED);
        Long shipped = orderRepository.countByOrderStatus(OrderStatus.SHIPPED);
        Long delivered = orderRepository.countByOrderStatus(OrderStatus.DELIVERED);

        return new AnalyticsResponseDto(placed,shipped,delivered,currentMonthOrders,previousMonthOrders,currentMonthEarnings,previousMonthEarnings);
    }

    public Long getTotalOrdersForMonth(int month ,int year){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH,1);

        calendar.set(Calendar.YEAR,0);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH,0);

        Date startOfMonth = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND,59);

        Date endOfMonth = calendar.getTime();

        List<Order> orderList = orderRepository.findByDateBetweenAndOrderStatus(startOfMonth,endOfMonth,OrderStatus.DELIVERED);

        return (long)orderList.size();
    }

    public Long getTotalEarningForMonth(int month ,int year){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH,1);

        calendar.set(Calendar.YEAR,0);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH,0);

        Date startOfMonth = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND,59);

        Date endOfMonth = calendar.getTime();

        List<Order> orderList = orderRepository.findByDateBetweenAndOrderStatus(startOfMonth,endOfMonth,OrderStatus.DELIVERED);

        Long sum = 0L;
        for(Order order:orderList){
            sum += order.getAmount();
        }
        return sum;
    }
}
