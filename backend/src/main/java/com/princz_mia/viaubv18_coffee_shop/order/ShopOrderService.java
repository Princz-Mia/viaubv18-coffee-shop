package com.princz_mia.viaubv18_coffee_shop.order;

import com.princz_mia.viaubv18_coffee_shop.address.AddressRepository;
import com.princz_mia.viaubv18_coffee_shop.exception.AppException;
import com.princz_mia.viaubv18_coffee_shop.order.status.OrderStatus;
import com.princz_mia.viaubv18_coffee_shop.order.status.OrderStatusRepository;
import com.princz_mia.viaubv18_coffee_shop.order.status.StatusName;
import com.princz_mia.viaubv18_coffee_shop.shipping_method.ShippingMethodRepository;
import com.princz_mia.viaubv18_coffee_shop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopOrderService {

    private final ShopOrderRepository shopOrderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final UserRepository userRepository;

    private final AddressRepository addressRepository;
    private final ShippingMethodRepository shippingMethodRepository;

    public ShopOrder createShopOrder(ShopOrderRequest shopOrderRequest) {
        var user = userRepository.findById(shopOrderRequest.getUserId())
                .orElseThrow(() -> new AppException("User is not found with matching id", HttpStatus.NOT_FOUND));

        var address = addressRepository.findById(shopOrderRequest.getAddressId())
                .orElseThrow(() -> new AppException("Address is not found with matching id", HttpStatus.NOT_FOUND));

        var method = shippingMethodRepository.findById(shopOrderRequest.getShippingMethodId())
                .orElseThrow(() -> new AppException("Shipping Method is not found with matching id", HttpStatus.NOT_FOUND));

        OrderStatus status = orderStatusRepository.findByNameIgnoreCase(StatusName.PENDING.name());

        ShopOrder shopOrder = ShopOrder.builder()
                .customer(user)
                .shippingAddress(address)
                .shippingMethod(method)
                .orderStatus(status)
                .orderDate(shopOrderRequest.getOrderDate())
                .orderTotal(shopOrderRequest.getOrderTotal())
                .firstName(shopOrderRequest.getFirstName())
                .lastName(shopOrderRequest.getLastName())
                .phoneNumber(shopOrderRequest.getPhoneNumber())
                .build();

        return shopOrderRepository.save(shopOrder);
    }

    public ShopOrder getPendingShopOrderByUserId(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User is not found with matching id", HttpStatus.NOT_FOUND));

        var shopOrder = shopOrderRepository.findByCustomerIdAndOrderStatusIgnoreCase(userId, StatusName.PENDING.name())
                .orElseThrow(() -> new AppException("Shop Order is not found with matching User Id and 'Pending' Status", HttpStatus.NOT_FOUND));

        // Implement AWAITING_PAYMENT logic later through refactoring
        //OrderStatus status = orderStatusRepository.findByNameIgnoreCase(StatusName.AWAITING_PAYMENT.name());
        //shopOrder.setOrderStatus(status);

        return shopOrderRepository.save(shopOrder);
    }
}
