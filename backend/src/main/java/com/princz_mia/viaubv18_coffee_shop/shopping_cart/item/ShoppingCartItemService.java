package com.princz_mia.viaubv18_coffee_shop.shopping_cart.item;

import com.princz_mia.viaubv18_coffee_shop.exception.AppException;
import com.princz_mia.viaubv18_coffee_shop.product.ProductRepository;
import com.princz_mia.viaubv18_coffee_shop.shopping_cart.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ShoppingCartItemService {

    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;

    public List<ShoppingCartItem> getShoppingCartItemsByCartId(Long cartId) {
        if (cartId == null || cartId < 0)
            throw new AppException("Cart Id is not valid", HttpStatus.BAD_REQUEST);

        var shoppingCart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new AppException("Cart is not found with matching Id", HttpStatus.NOT_FOUND));

        return shoppingCartItemRepository.findAllByShoppingCart(shoppingCart);
    }

    public ShoppingCartItem addShoppingCartItemToCart(ShoppingCartItemRequest shoppingCartItemRequest) {
        var shoppingCart = shoppingCartRepository.findById(shoppingCartItemRequest.getCartId())
                .orElseThrow(() -> new AppException("Cart is not found with matching Id", HttpStatus.NOT_FOUND));

        var product = productRepository.findById(shoppingCartItemRequest.getProductId())
                .orElseThrow(() -> new AppException("Product is not found with matching Id", HttpStatus.NOT_FOUND));

        var optionalShoppingCartItem = shoppingCartItemRepository.findByShoppingCartAndProduct(shoppingCart, product);

        if (optionalShoppingCartItem.isPresent()) {
            ShoppingCartItem shoppingCartItem = optionalShoppingCartItem.get();
            shoppingCartItem.setQty(shoppingCartItemRequest.getQty());
            return shoppingCartItemRepository.save(shoppingCartItem);
        }

        ShoppingCartItem newShoppingCartItem = ShoppingCartItem.builder()
                .shoppingCart(shoppingCart)
                .product(product)
                .qty(shoppingCartItemRequest.getQty())
                .build();

        return shoppingCartItemRepository.save(newShoppingCartItem);
    }

    public boolean removeShoppingCartItemToCart(ShoppingCartItemRequest shoppingCartItemRequest) {
        var shoppingCart = shoppingCartRepository.findById(shoppingCartItemRequest.getCartId())
                .orElseThrow(() -> new AppException("Cart is not found with matching Id", HttpStatus.NOT_FOUND));

        var product = productRepository.findById(shoppingCartItemRequest.getProductId())
                .orElseThrow(() -> new AppException("Product is not found with matching Id", HttpStatus.NOT_FOUND));

        var shoppingCartItem = shoppingCartItemRepository.findByShoppingCartAndProduct(shoppingCart, product)
                .orElseThrow(() -> new AppException("Cart Item is not found", HttpStatus.NOT_FOUND));

        shoppingCartItemRepository.delete(shoppingCartItem);
        return shoppingCartItemRepository.existsById(shoppingCartItem.getId());
    }
}
