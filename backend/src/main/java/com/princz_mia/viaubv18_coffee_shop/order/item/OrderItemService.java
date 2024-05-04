package com.princz_mia.viaubv18_coffee_shop.order.item;

import com.princz_mia.viaubv18_coffee_shop.exception.AppException;
import com.princz_mia.viaubv18_coffee_shop.order.ShopOrderRepository;
import com.princz_mia.viaubv18_coffee_shop.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final ShopOrderRepository shopOrderRepository;

    public OrderItem createOrderItem(OrderItemRequest orderItemRequest) {
        var product = productRepository.findById(orderItemRequest.getProductId())
                .orElseThrow(() -> new AppException("Product is not found with matching id", HttpStatus.NOT_FOUND));

        var shopOrder = shopOrderRepository.findById(orderItemRequest.getOrderId())
                .orElseThrow(() -> new AppException("Shop Order is not found with matching id", HttpStatus.NOT_FOUND));

        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .order(shopOrder)
                .qty(orderItemRequest.getQty())
                .price(orderItemRequest.getPrice())
                .build();

        return orderItemRepository.save(orderItem);
    }

    public List<OrderItem> getShopOrderItemsByShopOrderId(Long shopOrderId) {
        return orderItemRepository.findAllByOrderId(shopOrderId)
                .orElseThrow(() -> new AppException("No Order Item is found with matching Shop Order Id", HttpStatus.NOT_FOUND));
    }
}
