package org.icet.crm.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CartItemDto {
    private Long id;
    private Long price;
    private Long quantity;
    private Long productId;
    private Long orderId;
    private String productName;
    private Long userId;
}
