package com.princz_mia.viaubv18_coffee_shop.events;

import com.princz_mia.viaubv18_coffee_shop.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class UserEvent {

    private User user;
    private EventType type;
    private Map<?, ?> data;
}
