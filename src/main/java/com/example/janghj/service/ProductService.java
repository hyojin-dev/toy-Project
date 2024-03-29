package com.example.janghj.service;

import com.example.janghj.config.security.UserDetailsImpl;
import com.example.janghj.domain.Category;
import com.example.janghj.domain.Product.Pants;
import com.example.janghj.domain.Product.Product;
import com.example.janghj.domain.Product.Shoes;
import com.example.janghj.domain.Product.Top;
import com.example.janghj.repository.product.ProductRepository;
import com.example.janghj.web.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public Product testGetProduct(String productName) {
        return productRepository.findByName(productName);
    }

    public void registerProduct(ProductDto productDto) {
        /* Product 엔티티가 증가할 수록 해당 코드가 함께 증가합니다.
         * 개선되어야 할 코드 리팩토링 예정입니다.*/
        if (productDto.getCategory().toString().equals("TOP")) {
            Top top = new Top(productDto);
            productRepository.save(top);
        } else if (productDto.getCategory().toString().equals("PANTS")) {
            Pants pants = new Pants(productDto);
            productRepository.save(pants);
        } else if (productDto.getCategory().toString().equals("SHOES")) {
            Shoes shoes = new Shoes(productDto);
            productRepository.save(shoes);
        }
    }

    public Product getProduct(Long productId) {
        Product product = (Product) productRepository.getById(productId);

        if (product == null) {
            throw new NullPointerException("해당 상품이 존재하지 않습니다. productId = " + productId);
        }
        return product;
    }

    @Transactional(rollbackFor = Throwable.class)
    public Product updateProduct(UserDetailsImpl nowUser, Long productId, ProductDto productDto) {
        Product product = (Product) productRepository.getById(productId);
        product.updateProduct(productDto);

        productRepository.save(product);
        return product;
    }

    public List<Product> getProducts(Category category, String sort) {
        //    1.최신순(default), 2.상품만 검색, 3.상품 검색 + 최신순 검색
        if (category == null && sort == null) {
            return productRepository.findAllByCategory(category, Sort.by(Sort.Direction.DESC, "CreatedAt"));
        } else if (category != null && sort.equals("date")) {
            return productRepository.findAllByCategory(category, Sort.by(Sort.Direction.DESC, "CreatedAt"));
        }
        return productRepository.findAllByCategory(category);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}