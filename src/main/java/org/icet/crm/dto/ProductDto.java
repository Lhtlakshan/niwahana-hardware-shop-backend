package org.icet.crm.dto;

import lombok.Data;
import lombok.ToString;
import org.icet.crm.entity.Category;
import org.springframework.web.multipart.MultipartFile;

@Data
@ToString
public class ProductDto {
    private Long id;
    private String name;
    private Long price;
    private String description;
    private byte[] byteImg;
    private Long categoryId;
    private String categoryName;
    private MultipartFile img;
}
