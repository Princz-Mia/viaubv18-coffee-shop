package com.princz_mia.viaubv18_coffee_shop.product;

import com.princz_mia.viaubv18_coffee_shop.exception.AppException;
import com.princz_mia.viaubv18_coffee_shop.product.category.ProductCategoryRepository;
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
    private final ProductCategoryRepository productCategoryRepository;

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

    public ProductPageResponse getProductBySearchTerm(String searchTerm, int pageNumber, int pageSize) {
        if (searchTerm.isEmpty()) {
            return getPageOfProducts(pageNumber, pageSize);
        }

        String formattedSearchTerm = searchTerm.toLowerCase();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> products = productRepository.findAllByNameContainsIgnoreCase(formattedSearchTerm, pageable);
        List<Product> productList = products.getContent();

        if (productList.isEmpty()) {
            throw new AppException("There is no product currently available for your needs.", HttpStatus.NOT_FOUND);
        }

        return ProductPageResponse.builder()
                .content(productList)
                .pageNumber(products.getNumber())
                .pageSize(products.getSize())
                .totalElements(products.getTotalElements())
                .totalPages(products.getTotalPages())
                .isLast(products.isLast())
                .build();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException("Product is not found", HttpStatus.NOT_FOUND));
    }

    public Product getProductByName(String name) {
        return productRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new AppException("Product is not found", HttpStatus.NOT_FOUND));
    }

    public Product createNewProduct(ProductRequest productRequest) {
        if (productRequest.getId() != null)
            throw new AppException("New Product should not have an Id", HttpStatus.BAD_REQUEST);

        var optionalProduct = productRepository.findByNameIgnoreCase(productRequest.getName());
        if (optionalProduct.isPresent())
            throw new AppException("Product is already exists with matching name", HttpStatus.BAD_REQUEST);

        var category = productCategoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new AppException("Product Category is not found", HttpStatus.BAD_REQUEST));

        Product product = Product.builder()
                .name(productRequest.getName())
                .qtyInStock(productRequest.getQtyInStock())
                .price(productRequest.getPrice())
                .description(productRequest.getDescription())
                .category(category)
                .productImage(productRequest.getProductImage())
                .build();

        return productRepository.save(product);
    }

    public Product updateProduct(ProductRequest productRequest) {
        if (productRequest.getId() == null)
            throw new AppException("Product Id field is missing value", HttpStatus.BAD_REQUEST);

        var product = productRepository.findById(productRequest.getId())
                .orElseThrow(() -> new AppException("Product is not found", HttpStatus.NOT_FOUND));

        product.setName(productRequest.getName());
        product.setQtyInStock(productRequest.getQtyInStock());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        var category = productCategoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new AppException("Product Category is not found", HttpStatus.BAD_REQUEST));
        product.setCategory(category);
        product.setProductImage(productRequest.getProductImage());

        return productRepository.save(product);
    }

    public void removeById(Long id) {
        // TODO: Implement business logic to handle deletion of Product entity while keeping reference integrity in database
        if (id == null)
            throw new AppException("Invalid Product Id", HttpStatus.BAD_REQUEST);

        var product = productRepository.findById(id)
                .orElseThrow(() -> new AppException("Product is not found", HttpStatus.NOT_FOUND));

        product.setIsRemoved(true);
        productRepository.save(product);
    }
}
