package org.icet.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalyticsResponseDto {
    private Long placed;
    private Long shipped;
    private Long delivered;
    private Long currentMonthOrders;
    private Long previousMonthOrders;
    private Long currentMonthEarning;
    private Long previousMonthEarning;
}
