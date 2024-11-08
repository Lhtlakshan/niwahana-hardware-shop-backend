package org.icet.crm.service.customer.customerOrder;

import org.icet.crm.dto.OrderDto;

import java.util.List;

public interface CustomerOrderService {
    List<OrderDto> getAllPlacedOrders();
}
