package org.icet.crm.service.admin.category.impl;

import lombok.RequiredArgsConstructor;
import org.icet.crm.dto.CategoryDto;
import org.icet.crm.entity.Category;
import org.icet.crm.repository.CategoryRepository;
import org.icet.crm.service.admin.category.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    @Override
    public Category createCategory(CategoryDto categoryDto){
        return categoryRepository.save(modelMapper.map(categoryDto , Category.class));
    }

    @Override
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }
}
