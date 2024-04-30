package com.princz_mia.viaubv18_coffee_shop.shopping_cart;

import com.princz_mia.viaubv18_coffee_shop.exception.AppException;
import com.princz_mia.viaubv18_coffee_shop.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public void createShoppingCartForVerifiedUser(User user) {
        if (!user.isEnabled())
            throw new AppException("User is not verified yet. Could not create ShoppingCart", HttpStatus.BAD_REQUEST);

        ShoppingCart shoppingCart = new ShoppingCart(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
