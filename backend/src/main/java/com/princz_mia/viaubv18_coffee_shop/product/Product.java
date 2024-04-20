package com.princz_mia.viaubv18_coffee_shop.product;

import com.princz_mia.viaubv18_coffee_shop.audit.Auditable;
import com.princz_mia.viaubv18_coffee_shop.product_category.ProductCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends Auditable {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private ProductCategory category;

    private String name;
    private String description;
    private String productImage;
    private Integer qtyInStock;
    private Double price;
}
