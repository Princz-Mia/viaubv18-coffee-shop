package com.princz_mia.viaubv18_coffee_shop.product.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product/category")
@RequiredArgsConstructor
@Slf4j
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @GetMapping(path = "/getAllCategory")
    public ResponseEntity<List<ProductCategory>> getAllCategory() {
        List<ProductCategory> categories = productCategoryService.getAllCategory();
        return ResponseEntity.ok(categories);
    }
}
