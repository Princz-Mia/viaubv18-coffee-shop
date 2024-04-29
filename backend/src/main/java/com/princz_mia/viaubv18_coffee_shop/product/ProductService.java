package com.princz_mia.viaubv18_coffee_shop.product;

import com.princz_mia.viaubv18_coffee_shop.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductPageResponse getPageOfProducts(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> products = productRepository.findAll(pageable);
        List<Product> productList = products.getContent();

        return ProductPageResponse.builder()
                .content(productList)
                .pageNumber(products.getNumber())
                .pageSize(products.getSize())
                .totalElements(products.getTotalElements())
                .totalPages(products.getTotalPages())
                .isLast(products.isLast())
                .build();
    }

    public Product getProductByName(String name) {
        var product = productRepository.findByNameIgnoreCase(name);
        return product.orElseThrow(() -> new AppException("Product is not found", HttpStatus.NOT_FOUND));
    }
}
