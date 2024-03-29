package com.example.janghj.web.dto;

import com.example.janghj.domain.Category;
import com.example.janghj.domain.Product.ProductColor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductDto {
    String name = "";
    int price = 0;
    int stockQuantity = 0;
    Category category; // TOP, PANTS, OUTER, SHOES, BAG
    ProductColor productColor; // RED, ORANGE, YELLOW, GREEN, BLUE, NAVY, PURPLE
    int size = 0;

    // Test 용도
    public ProductDto(String name, int price, int stockQuantity, Category category, ProductColor productColor, int size) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.productColor = productColor;
        this.size = size;
    }
}