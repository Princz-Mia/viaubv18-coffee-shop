package com.princz_mia.viaubv18_coffee_shop.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<ProductPageResponse> getPageOfProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "8", required = false) int pageSize
    ) {
        ProductPageResponse productPage = productService.getPageOfProducts(pageNumber, pageSize);
        return ResponseEntity.ok(productPage);
    }


    @GetMapping("/product/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable(value = "name") String name) {
        Product product = productService.getProductByName(name);
        return ResponseEntity.ok(product);
    }
}
