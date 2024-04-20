package com.princz_mia.viaubv18_coffee_shop.product_category;

import com.princz_mia.viaubv18_coffee_shop.audit.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
