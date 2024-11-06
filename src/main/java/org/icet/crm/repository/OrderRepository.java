package org.icet.crm.repository;

import org.icet.crm.entity.Order;
import org.icet.crm.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);
}
