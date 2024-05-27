package com.princz_mia.viaubv18_coffee_shop.shop_order.item;

import com.princz_mia.viaubv18_coffee_shop.exception.AppException;
import com.princz_mia.viaubv18_coffee_shop.shop_order.ShopOrderRepository;
import com.princz_mia.viaubv18_coffee_shop.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopOrderItemService {

    private final ShopOrderItemRepository shopOrderItemRepository;
    private final ProductRepository productRepository;
    private final ShopOrderRepository shopOrderRepository;

    public ShopOrderItem createOrderItem(ShopOrderItemRequest shopOrderItemRequest) {
        var product = productRepository.findById(shopOrderItemRequest.getProductId())
                .orElseThrow(() -> new AppException("Product is not found with matching id", HttpStatus.NOT_FOUND));

        var shopOrder = shopOrderRepository.findById(shopOrderItemRequest.getShopOrderId())
                .orElseThrow(() -> new AppException("Shop Order is not found with matching id", HttpStatus.NOT_FOUND));

        ShopOrderItem shopOrderItem = com.princz_mia.viaubv18_coffee_shop.shop_order.item.ShopOrderItem.builder()
                .product(product)
                .shopOrder(shopOrder)
                .qty(shopOrderItemRequest.getQty())
                .price(shopOrderItemRequest.getPrice())
                .build();

        return shopOrderItemRepository.save(shopOrderItem);
    }

    public List<ShopOrderItem> getShopOrderItemsByShopOrderId(Long shopOrderId) {
        return shopOrderItemRepository.findAllByShopOrderId(shopOrderId)
                .orElseThrow(() -> new AppException("No Order Item is found with matching Shop Order Id", HttpStatus.NOT_FOUND));
    }
}
