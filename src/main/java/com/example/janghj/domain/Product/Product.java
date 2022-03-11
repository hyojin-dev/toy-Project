package com.example.janghj.domain.Product;

import com.example.janghj.domain.Category;
import com.example.janghj.domain.Order;
import com.example.janghj.domain.Timestamped;
import com.example.janghj.domain.User.UserLikes;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

////@Inheritance(strategy = InheritanceType.JOINED)
////@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@Entity
//// 부모 테이블을 구분할 구분자 컬럼이름을 지어준다.
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "PRODUCT")
//@Getter @Setter
//@Table
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn(name = "dtype")
@Getter
public abstract class Product extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")// 반드시 값을 가지도록 합니다.
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private int stockQuantity;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProductColor productColor;

    public Product(String name, int price, int stockQuantity, Category category, ProductColor productColor) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.productColor = productColor;
    }
}
