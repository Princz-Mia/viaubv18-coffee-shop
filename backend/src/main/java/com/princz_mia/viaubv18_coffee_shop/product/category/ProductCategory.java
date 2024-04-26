package com.princz_mia.viaubv18_coffee_shop.product.category;

import com.princz_mia.viaubv18_coffee_shop.audit.Auditable;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory extends Auditable {

    private String name;
}
