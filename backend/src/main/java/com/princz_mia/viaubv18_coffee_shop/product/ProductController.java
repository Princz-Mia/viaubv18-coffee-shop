package com.princz_mia.viaubv18_coffee_shop.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<ProductPageResponse> getPageOfProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "8", required = false) int pageSize
    ) {
        ProductPageResponse productPage = productService.getPageOfProducts(pageNumber, pageSize);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/search/{searchTerm}")
    public ResponseEntity<ProductPageResponse> getProductByName(
            @PathVariable(value = "searchTerm") String searchTerm,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "8", required = false) int pageSize
    ) {
        ProductPageResponse productPage = productService.getProductBySearchTerm(searchTerm, pageNumber, pageSize);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable(value = "id") Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable(value = "name") String name) {
        Product product = productService.getProductByName(name);
        return ResponseEntity.ok(product);
    }

    @PostMapping(path = "/createNewProduct")
    public ResponseEntity<Product> createNewProduct(@RequestBody @Valid ProductRequest productRequest) {
        Product product = productService.createNewProduct(productRequest);
        return ResponseEntity.ok(product);
    }

    @PostMapping(path = "/updateProduct")
    public ResponseEntity<Product> updateProduct(@RequestBody @Valid ProductRequest productRequest) {
        Product product = productService.updateProduct(productRequest);
        return ResponseEntity.ok(product);
    }

    @PostMapping(path = "/deleteById/{id}")
    public ResponseEntity<?> removeById(@PathVariable(value = "id") Long id) {
        productService.removeById(id);
        return ResponseEntity.ok(true);
    }
}
