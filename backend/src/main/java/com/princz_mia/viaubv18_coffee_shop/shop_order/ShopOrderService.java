package com.princz_mia.viaubv18_coffee_shop.shop_order;

import com.princz_mia.viaubv18_coffee_shop.address.AddressRepository;
import com.princz_mia.viaubv18_coffee_shop.exception.AppException;
import com.princz_mia.viaubv18_coffee_shop.shop_order.item.ShopOrderItemRepository;
import com.princz_mia.viaubv18_coffee_shop.shop_order.status.ShopOrderStatus;
import com.princz_mia.viaubv18_coffee_shop.shop_order.status.ShopOrderStatusRepository;
import com.princz_mia.viaubv18_coffee_shop.shop_order.status.StatusName;
import com.princz_mia.viaubv18_coffee_shop.shipping_method.ShippingMethodRepository;
import com.princz_mia.viaubv18_coffee_shop.user.UserRepository;
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
public class ShopOrderService {

    private final ShopOrderRepository shopOrderRepository;
    private final ShopOrderStatusRepository shopOrderStatusRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ShippingMethodRepository shippingMethodRepository;
    private ShopOrderItemRepository shopOrderItemRepository;

    public ShopOrder createShopOrder(ShopOrderRequest shopOrderRequest) {
        var user = userRepository.findById(shopOrderRequest.getUserId())
                .orElseThrow(() -> new AppException("User is not found with matching id", HttpStatus.BAD_REQUEST));

        var address = addressRepository.findById(shopOrderRequest.getAddressId())
                .orElseThrow(() -> new AppException("Address is not found with matching id", HttpStatus.BAD_REQUEST));

        var method = shippingMethodRepository.findById(shopOrderRequest.getShippingMethodId())
                .orElseThrow(() -> new AppException("Shipping Method is not found with matching id", HttpStatus.BAD_REQUEST));

        ShopOrderStatus status = shopOrderStatusRepository.findByNameIgnoreCase(StatusName.PENDING.name())
                .orElseThrow(() -> new AppException("Shop Order Status is not found with matching name", HttpStatus.BAD_REQUEST));

        ShopOrder shopOrder = ShopOrder.builder()
                .customer(user)
                .shippingAddress(address)
                .shippingMethod(method)
                .shopOrderStatus(status)
                .orderDate(shopOrderRequest.getOrderDate())
                .orderTotal(shopOrderRequest.getOrderTotal())
                .firstName(shopOrderRequest.getFirstName())
                .lastName(shopOrderRequest.getLastName())
                .phoneNumber(shopOrderRequest.getPhoneNumber())
                .paymentId(null)
                .build();

        return shopOrderRepository.save(shopOrder);
    }

    public ShopOrder getPendingShopOrderByUserId(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User is not found with matching id", HttpStatus.NOT_FOUND));

        var shopOrder = shopOrderRepository.findByCustomerIdAndShopOrderStatusNameIgnoreCase(userId, StatusName.PENDING.name())
                .orElseThrow(() -> new AppException("Shop Order is not found with matching User Id and 'Pending' Status", HttpStatus.NOT_FOUND));

        // Implement AWAITING_PAYMENT logic later through refactoring
        // ShopOrderStatus status = shopOrderStatusRepository.findByNameIgnoreCase(StatusName.AWAITING_PAYMENT.name());
        // shopOrder.setShopOrderStatus(status);

        return shopOrderRepository.save(shopOrder);
    }

    public ShopOrder processPayTransaction(PaymentRequest paymentRequest) {
        var shopOrder = shopOrderRepository.findById(paymentRequest.getShopOrderId())
                .orElseThrow(() -> new AppException("Shop Order is not found with matching User Id and 'Pending' Status", HttpStatus.NOT_FOUND));

        ShopOrderStatus currentStatus = shopOrder.getShopOrderStatus();
        if (!currentStatus.getName().equals(StatusName.PENDING.name()) && shopOrder.getPaymentId() == null) {
            throw new AppException("Shop Order is already payed", HttpStatus.NOT_MODIFIED);
        }

        ShopOrderStatus modifiedStatus = shopOrderStatusRepository.findByNameIgnoreCase(StatusName.PAYMENT_FULFILLED.name())
                .orElseThrow(() -> new AppException("Shop Order Status is not found with matching name", HttpStatus.BAD_REQUEST));

        shopOrder.setShopOrderStatus(modifiedStatus);
        shopOrder.setPaymentId(paymentRequest.getPaymentId());

        return shopOrderRepository.save(shopOrder);
    }

    public Boolean hasUserPendingShopOrder(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User is not found with matching id", HttpStatus.NOT_FOUND));

        var shopOrder = shopOrderRepository.findByCustomerIdAndShopOrderStatusNameIgnoreCase(userId, StatusName.PENDING.name());
        return shopOrder.isPresent();
    }

    public List<ShopOrder> getShopOrdersByUserId(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User is not found with matching id", HttpStatus.NOT_FOUND));

        return shopOrderRepository.findByCustomer(user)
                .orElseThrow(() -> new AppException("No Shop Order is found from User", HttpStatus.NOT_FOUND));
    }

    public ShopOrder getShopOrderById(Long orderId) {
        return shopOrderRepository.findById(orderId)
                .orElseThrow(() -> new AppException("Shop Order is not found with matching id", HttpStatus.NOT_FOUND));
    }

    public ShopOrderPageResponse getPageOfShopOrders(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ShopOrder> shopOrders = shopOrderRepository.findAll(pageable);
        List<ShopOrder> shopOrderList = shopOrders.getContent();

        return ShopOrderPageResponse.builder()
                .content(shopOrderList)
                .pageNumber(shopOrders.getNumber())
                .pageSize(shopOrders.getSize())
                .totalElements(shopOrders.getTotalElements())
                .totalPages(shopOrders.getTotalPages())
                .isLast(shopOrders.isLast())
                .build();
    }

    public ShopOrder updateShopOrderStatus(ShopOrderRequest shopOrderRequest) {
        if (shopOrderRequest.getId() == null)
            throw new AppException("Shop Order Id must not be null", HttpStatus.BAD_REQUEST);

        var shopOrder = shopOrderRepository.findById(shopOrderRequest.getId())
                .orElseThrow(() -> new AppException("Shop Order is not found with matching id", HttpStatus.NOT_FOUND));

        var shopOrderStatus = shopOrderStatusRepository.findById(shopOrderRequest.getShopOrderStatusId())
                .orElseThrow(() -> new AppException("Shop Order Status is not found with matching id", HttpStatus.BAD_REQUEST));

        shopOrder.setShopOrderStatus(shopOrderStatus);
        return shopOrderRepository.save(shopOrder);
    }

    public void deleteShopOrderById(Long shopOrderId) {
        var shopOrder = shopOrderRepository.findById(shopOrderId)
                .orElseThrow(() -> new AppException("Shop Order is not found with matching id", HttpStatus.NOT_FOUND));

        shopOrderItemRepository.deleteAllByShopOrderId(shopOrderId);
        shopOrderRepository.deleteById(shopOrder.getId());
    }
}
