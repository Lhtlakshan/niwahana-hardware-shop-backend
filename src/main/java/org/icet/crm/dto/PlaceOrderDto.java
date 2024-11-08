package org.icet.crm.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PlaceOrderDto {
    private Long userId;
    private String address;
    private String orderDescription;
}
