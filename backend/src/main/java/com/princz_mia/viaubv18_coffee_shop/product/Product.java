package com.princz_mia.viaubv18_coffee_shop.product;

import com.princz_mia.viaubv18_coffee_shop.product.category.ProductCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private ProductCategory category;

    private String name;
    private String description;
    private String productImage;
    private Integer qtyInStock;
    private Double price;
}
