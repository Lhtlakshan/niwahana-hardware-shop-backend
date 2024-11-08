package org.icet.crm.dto;

import lombok.Data;
import lombok.ToString;
import org.icet.crm.enums.OrderStatus;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@ToString
public class OrderDto {
    private Long id;
    private String orderDescription;
    private Date date;
    private Long amount;
    private String address;
    private String payment;
    private OrderStatus orderStatus;
    private Long totalAmount;
    private Long discount;
    private UUID trackingId;
    private String userName;
    private List<CartItemDto> cartItems;
    private String couponName;
}
