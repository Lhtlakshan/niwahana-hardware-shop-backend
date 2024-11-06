package org.icet.crm.service.admin.product.impl;

import lombok.RequiredArgsConstructor;
import org.icet.crm.dto.CategoryDto;
import org.icet.crm.dto.ProductDto;
import org.icet.crm.entity.Category;
import org.icet.crm.entity.Product;
import org.icet.crm.repository.CategoryRepository;
import org.icet.crm.repository.ProductRepository;
import org.icet.crm.service.admin.product.AdminProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductDto addProduct(ProductDto productDto){
        Product product = productRepository.save(modelMapper.map(productDto,Product.class));
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow();
        product.setCategory(category);
        ProductDto productDto1 = modelMapper.map(productRepository.save(product), ProductDto.class);
        productDto1.setCategoryName(category.getName());
        return productDto1;
    }

    @Override
    public List<ProductDto> getAllProducts(){
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();

        products.forEach(product->{
            productDtos.add(modelMapper.map(product, ProductDto.class));
        });

        return productDtos;
    }

    @Override
    public List<ProductDto> getAllProductByName(String name){
        List<Product> products = productRepository.findAllByNameContaining(name);
        List<ProductDto> productDtos = new ArrayList<>();

        products.forEach(product->{
            productDtos.add(modelMapper.map(product, ProductDto.class));
        });

        return productDtos;
    }
}
