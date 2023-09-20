package com.hugorithm.hopfencraft.dto.user;

import com.hugorithm.hopfencraft.model.CartItem;
import com.hugorithm.hopfencraft.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OAuth2ApplicationUserDTO {
    private Long userId;
    private String name;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private List<CartItem> cartItems;
    private List<Order> orders;
    private Map<String, String> attributes;
}
