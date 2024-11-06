package org.icet.crm.service.customer.impl;

import lombok.RequiredArgsConstructor;
import org.icet.crm.dto.ProductDto;
import org.icet.crm.entity.Product;
import org.icet.crm.repository.ProductRepository;
import org.icet.crm.service.customer.CustomerProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerProductServiceImpl implements CustomerProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
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
