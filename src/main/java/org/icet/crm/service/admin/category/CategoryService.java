package org.icet.crm.service.admin.category;

import org.icet.crm.dto.CategoryDto;
import org.icet.crm.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryDto categoryDto);
    List<Category> getAllCategories();
}
