package org.icet.crm.service.customer;

import org.icet.crm.dto.ProductDto;

import java.util.List;

public interface CustomerProductService {
    List<ProductDto> getAllProducts();
    List<ProductDto> getAllProductByName(String name);
}
