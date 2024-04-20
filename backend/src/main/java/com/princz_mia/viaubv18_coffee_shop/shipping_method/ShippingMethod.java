package com.princz_mia.viaubv18_coffee_shop.shipping_method;

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
public class ShippingMethod extends Auditable {

    private String name;
    private Double price;
}
