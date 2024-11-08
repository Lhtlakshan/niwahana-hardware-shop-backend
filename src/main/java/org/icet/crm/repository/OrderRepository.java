package org.icet.crm.repository;

import org.icet.crm.entity.Order;
import org.icet.crm.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);
    List<Order> findAllByOrderStatusIn(List<OrderStatus> orderStatusList);
    List<Order> findAllByUserIdAndOrderStatusIn(Long userId,List<OrderStatus> orderStatus);
}
