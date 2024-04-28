package com.princz_mia.viaubv18_coffee_shop.events;

import com.princz_mia.viaubv18_coffee_shop.email.EmailService;
import com.princz_mia.viaubv18_coffee_shop.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventListener {

    private final EmailService emailService;

    @EventListener
    public void onUserEvent(UserEvent event) {
        User user = event.getUser();
        switch (event.getType()) {
            case REGISTRATION -> emailService.sendNewAccountEmail(
                    user.getFirstName() + " " + user.getLastName(),
                    user.getEmail(),
                    event.getData().get("key").toString()
            );
            case RESET_PASSWORD -> emailService.sendPasswordResetEmail(
                    user.getFirstName() + " " + user.getLastName(),
                    user.getEmail(),
                    event.getData().get("key").toString()
            );
            default -> {}
        }
    }
}
