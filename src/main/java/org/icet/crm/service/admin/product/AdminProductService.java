package org.icet.crm.service.admin.product;

import org.icet.crm.dto.ProductDto;

import java.util.List;

public interface AdminProductService {
     ProductDto addProduct(ProductDto productDto);
     List<ProductDto> getAllProducts();
     List<ProductDto> getAllProductByName(String name);
     boolean deleteProduct(Long id);
     ProductDto geyProductById(Long productId);
     ProductDto updateProduct(Long productId ,ProductDto productDto);
}
