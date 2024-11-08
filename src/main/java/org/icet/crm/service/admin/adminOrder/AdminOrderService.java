package org.icet.crm.service.admin.adminOrder;

import org.icet.crm.dto.AnalyticsResponseDto;
import org.icet.crm.dto.OrderDto;

import java.util.List;

public interface AdminOrderService {
    List<OrderDto> getAllPlacedOrders();
    OrderDto changeOrderStatus(Long orderId , String status);
    AnalyticsResponseDto calculateAnalytics();
}
