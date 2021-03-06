package com.example.janghj.domain.Product;

import com.example.janghj.web.dto.ProductDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("SHOES")
public class Shoes extends Product {

    private int shoesSize;

    public Shoes(ProductDto productDto) {
        super(productDto.getName(), productDto.getPrice(), productDto.getStockQuantity(), productDto.getCategory(), productDto.getProductColor());
        this.shoesSize = productDto.getSize();
    }
}
